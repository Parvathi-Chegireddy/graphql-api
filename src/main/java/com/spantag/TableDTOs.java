package com.spantag;

import java.util.List;
import java.util.Objects;

public final class TableDTOs {

    private TableDTOs() {}

    public static class TableInfo {
        private String  tableName;
        private Integer rowCount;

        public TableInfo() {}

        public TableInfo(String tableName, Integer rowCount) {
            this.tableName = tableName;
            this.rowCount  = rowCount;
        }

        public String  getTableName() { return tableName; }
        public Integer getRowCount()  { return rowCount;  }
        public void setTableName(String tableName) { this.tableName = tableName; }
        public void setRowCount(Integer rowCount)  { this.rowCount  = rowCount;  }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableInfo)) return false;
            TableInfo other = (TableInfo) o;
            return Objects.equals(tableName, other.tableName)
                && Objects.equals(rowCount,  other.rowCount);
        }

        @Override
        public int hashCode() { return Objects.hash(tableName, rowCount); }

        @Override
        public String toString() {
            return "TableInfo{tableName='" + tableName + "', rowCount=" + rowCount + '}';
        }

        public static Builder builder() { return new Builder(); }

        public static final class Builder {
            private String  tableName;
            private Integer rowCount;

            private Builder() {}

            public Builder tableName(String tableName) { this.tableName = tableName; return this; }
            public Builder rowCount(Integer rowCount)  { this.rowCount  = rowCount;  return this; }

            public TableInfo build() { return new TableInfo(tableName, rowCount); }
        }
    }

    public static class ColumnInfo {
        private String  columnName;
        private String  dataType;
        private Boolean isNullable;
        private String  columnDefault;
        private Boolean isPrimaryKey;

        public ColumnInfo() {}

        public ColumnInfo(String columnName, String dataType, Boolean isNullable,
                          String columnDefault, Boolean isPrimaryKey) {
            this.columnName    = columnName;
            this.dataType      = dataType;
            this.isNullable    = isNullable;
            this.columnDefault = columnDefault;
            this.isPrimaryKey  = isPrimaryKey;
        }

        public String  getColumnName()    { return columnName;    }
        public String  getDataType()      { return dataType;      }
        public Boolean getIsNullable()    { return isNullable;    }
        public String  getColumnDefault() { return columnDefault; }
        public Boolean getIsPrimaryKey()  { return isPrimaryKey;  }

        public void setColumnName(String columnName)       { this.columnName    = columnName;    }
        public void setDataType(String dataType)           { this.dataType      = dataType;      }
        public void setIsNullable(Boolean isNullable)      { this.isNullable    = isNullable;    }
        public void setColumnDefault(String columnDefault) { this.columnDefault = columnDefault; }
        public void setIsPrimaryKey(Boolean isPrimaryKey)  { this.isPrimaryKey  = isPrimaryKey;  }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ColumnInfo)) return false;
            ColumnInfo other = (ColumnInfo) o;
            return Objects.equals(columnName,    other.columnName)
                && Objects.equals(dataType,      other.dataType)
                && Objects.equals(isNullable,    other.isNullable)
                && Objects.equals(columnDefault, other.columnDefault)
                && Objects.equals(isPrimaryKey,  other.isPrimaryKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(columnName, dataType, isNullable, columnDefault, isPrimaryKey);
        }

        @Override
        public String toString() {
            return "ColumnInfo{" +
                   "columnName='"    + columnName    + '\'' +
                   ", dataType='"    + dataType      + '\'' +
                   ", isNullable="   + isNullable    +
                   ", columnDefault='"+ columnDefault + '\'' +
                   ", isPrimaryKey=" + isPrimaryKey  +
                   '}';
        }

        public static Builder builder() { return new Builder(); }

        public static final class Builder {
            private String  columnName;
            private String  dataType;
            private Boolean isNullable;
            private String  columnDefault;
            private Boolean isPrimaryKey;

            private Builder() {}

            public Builder columnName(String columnName)       { this.columnName    = columnName;    return this; }
            public Builder dataType(String dataType)           { this.dataType      = dataType;      return this; }
            public Builder isNullable(Boolean isNullable)      { this.isNullable    = isNullable;    return this; }
            public Builder columnDefault(String columnDefault) { this.columnDefault = columnDefault; return this; }
            public Builder isPrimaryKey(Boolean isPrimaryKey)  { this.isPrimaryKey  = isPrimaryKey;  return this; }

            public ColumnInfo build() {
                return new ColumnInfo(columnName, dataType, isNullable, columnDefault, isPrimaryKey);
            }
        }
    }


    public static class TableStructure {
        private String           tableName;
        private List<ColumnInfo> columns;

        public TableStructure() {}

        public TableStructure(String tableName, List<ColumnInfo> columns) {
            this.tableName = tableName;
            this.columns   = columns;
        }

        public String           getTableName() { return tableName; }
        public List<ColumnInfo> getColumns()   { return columns;   }
        public void setTableName(String tableName)         { this.tableName = tableName; }
        public void setColumns(List<ColumnInfo> columns)   { this.columns   = columns;   }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableStructure)) return false;
            TableStructure other = (TableStructure) o;
            return Objects.equals(tableName, other.tableName)
                && Objects.equals(columns,   other.columns);
        }

        @Override
        public int hashCode() { return Objects.hash(tableName, columns); }

        @Override
        public String toString() {
            return "TableStructure{tableName='" + tableName +
                   "', columns=" + columns + '}';
        }

        public static Builder builder() { return new Builder(); }

        public static final class Builder {
            private String           tableName;
            private List<ColumnInfo> columns;

            private Builder() {}

            public Builder tableName(String tableName)       { this.tableName = tableName; return this; }
            public Builder columns(List<ColumnInfo> columns) { this.columns   = columns;   return this; }

            public TableStructure build() { return new TableStructure(tableName, columns); }
        }
    }

    public static class TableRecords {
        private String       tableName;
        private List<String> columns;   // column names
        private List<String> rows;      // each row serialised as a JSON string
        private Integer      totalRows;

        public TableRecords() {}

        public TableRecords(String tableName, List<String> columns,
                            List<String> rows, Integer totalRows) {
            this.tableName = tableName;
            this.columns   = columns;
            this.rows      = rows;
            this.totalRows = totalRows;
        }

        public String       getTableName() { return tableName; }
        public List<String> getColumns()   { return columns;   }
        public List<String> getRows()      { return rows;      }
        public Integer      getTotalRows() { return totalRows; }

        public void setTableName(String tableName)   { this.tableName = tableName; }
        public void setColumns(List<String> columns) { this.columns   = columns;   }
        public void setRows(List<String> rows)       { this.rows      = rows;      }
        public void setTotalRows(Integer totalRows)  { this.totalRows = totalRows; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableRecords)) return false;
            TableRecords other = (TableRecords) o;
            return Objects.equals(tableName, other.tableName)
                && Objects.equals(columns,   other.columns)
                && Objects.equals(rows,      other.rows)
                && Objects.equals(totalRows, other.totalRows);
        }

        @Override
        public int hashCode() { return Objects.hash(tableName, columns, rows, totalRows); }

        @Override
        public String toString() {
            return "TableRecords{" +
                   "tableName='" + tableName +
                   "', columns=" + columns   +
                   ", totalRows=" + totalRows +
                   '}';
        }

        public static Builder builder() { return new Builder(); }

        public static final class Builder {
            private String       tableName;
            private List<String> columns;
            private List<String> rows;
            private Integer      totalRows;

            private Builder() {}

            public Builder tableName(String tableName)   { this.tableName = tableName; return this; }
            public Builder columns(List<String> columns) { this.columns   = columns;   return this; }
            public Builder rows(List<String> rows)       { this.rows      = rows;      return this; }
            public Builder totalRows(Integer totalRows)  { this.totalRows = totalRows; return this; }

            public TableRecords build() {
                return new TableRecords(tableName, columns, rows, totalRows);
            }
        }
    }
}
