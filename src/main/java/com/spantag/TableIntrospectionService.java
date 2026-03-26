package com.spantag;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Queries PostgreSQL's {@code information_schema} and live tables
 * to produce metadata used by the Table Explorer page.
 *
 * Exposes only the three application tables to avoid leaking system info.
 */
@Service
public class TableIntrospectionService {

    private static final Logger log = LoggerFactory.getLogger(TableIntrospectionService.class);

    private final DataSource   dataSource;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TableIntrospectionService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Only these tables are visible to the Table Explorer. */
    private static final List<String> ALLOWED_TABLES =
            List.of("department", "employee", "employee_profile");

    // ─── Public API ───────────────────────────────────────────────

    public List<TableDTOs.TableInfo> getAvailableTables() {
        List<TableDTOs.TableInfo> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            for (String table : ALLOWED_TABLES) {
                result.add(TableDTOs.TableInfo.builder()
                        .tableName(table)
                        .rowCount(countRows(conn, table))
                        .build());
            }
        } catch (SQLException ex) {
            log.error("Failed to list tables", ex);
        }
        return result;
    }

    public TableDTOs.TableStructure getTableStructure(String tableName) {
        String tbl = sanitize(tableName);

        Set<String> pks = new HashSet<>();
        List<TableDTOs.ColumnInfo> cols = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {

            // Primary keys
            String pkSql = """
                SELECT kcu.column_name
                FROM information_schema.table_constraints tc
                JOIN information_schema.key_column_usage  kcu
                  ON tc.constraint_name = kcu.constraint_name
                 AND tc.table_schema    = kcu.table_schema
                WHERE tc.constraint_type = 'PRIMARY KEY'
                  AND tc.table_schema    = 'public'
                  AND tc.table_name      = ?
                """;
            try (PreparedStatement ps = conn.prepareStatement(pkSql)) {
                ps.setString(1, tbl);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) pks.add(rs.getString(1).toLowerCase());
                }
            }

            // Columns
            String colSql = """
                SELECT column_name, data_type, is_nullable, column_default
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name   = ?
                ORDER BY ordinal_position
                """;
            try (PreparedStatement ps = conn.prepareStatement(colSql)) {
                ps.setString(1, tbl);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String col = rs.getString("column_name");
                        cols.add(TableDTOs.ColumnInfo.builder()
                                .columnName(col)
                                .dataType(rs.getString("data_type"))
                                .isNullable("YES".equals(rs.getString("is_nullable")))
                                .columnDefault(rs.getString("column_default"))
                                .isPrimaryKey(pks.contains(col.toLowerCase()))
                                .build());
                    }
                }
            }

        } catch (SQLException ex) {
            log.error("Failed to describe table: {}", tbl, ex);
            throw new RuntimeException("Error fetching table structure: " + tbl, ex);
        }

        return TableDTOs.TableStructure.builder()
                .tableName(tbl)
                .columns(cols)
                .build();
    }

    public TableDTOs.TableRecords getTableRecords(String tableName) {
        String tbl      = sanitize(tableName);
        List<String> columns = new ArrayList<>();
        List<String> rows    = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM " + tbl + " LIMIT 10");
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) columns.add(meta.getColumnName(i));

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    Object val = rs.getObject(i);
                    row.put(columns.get(i - 1), val == null ? null : val.toString());
                }
                try {
                    rows.add(objectMapper.writeValueAsString(row));
                } catch (Exception ex) {
                    rows.add("{}");
                }
            }
        } catch (SQLException ex) {
            log.error("Failed to read records from table: {}", tbl, ex);
            throw new RuntimeException("Error fetching records: " + tbl, ex);
        }

        int total = 0;
        try (Connection conn = dataSource.getConnection()) {
            total = countRows(conn, tbl);
        } catch (SQLException ignored) {}

        return TableDTOs.TableRecords.builder()
                .tableName(tbl)
                .columns(columns)
                .rows(rows)
                .totalRows(total)
                .build();
    }

    // ─── Helpers ──────────────────────────────────────────────────

    private String sanitize(String tableName) {
        String lower = tableName.toLowerCase().trim();
        if (!ALLOWED_TABLES.contains(lower)) {
            throw new IllegalArgumentException("Table not accessible: " + tableName);
        }
        return lower;
    }

    private int countRows(Connection conn, String table) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}