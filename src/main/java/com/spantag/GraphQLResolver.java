package com.spantag;

import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class GraphQLResolver {

    private final DepartmentService         departmentService;
    private final EmployeeService           employeeService;
    private final EmployeeProfileService    profileService;
    private final TableIntrospectionService tableService;

    public GraphQLResolver(DepartmentService departmentService,
                           EmployeeService employeeService,
                           EmployeeProfileService profileService,
                           TableIntrospectionService tableService) {
        this.departmentService = departmentService;
        this.employeeService   = employeeService;
        this.profileService    = profileService;
        this.tableService      = tableService;
    }


    @QueryMapping
    public List<Department> departments() {
        return departmentService.findAll();
    }

    @QueryMapping
    public Department department(@Argument Long id) {
        return departmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
    }


    @QueryMapping
    public List<Employee> employees() {
        return employeeService.findAll();
    }

    @QueryMapping
    public Employee employee(@Argument Long id) {
        return employeeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    @QueryMapping
    public List<Employee> employeesByDepartment(@Argument Long departmentId) {
        return employeeService.findByDepartmentId(departmentId);
    }


    @QueryMapping
    public EmployeeProfile employeeProfile(@Argument Long employeeId) {
        return profileService.findByEmployeeId(employeeId).orElse(null);
    }

    @QueryMapping
    public List<TableDTOs.TableInfo> availableTables() {
        return tableService.getAvailableTables();
    }

    @QueryMapping
    public TableDTOs.TableStructure tableStructure(@Argument String tableName) {
        return tableService.getTableStructure(tableName);
    }

    @QueryMapping
    public TableDTOs.TableRecords tableRecords(@Argument String tableName) {
        return tableService.getTableRecords(tableName);
    }

    @SchemaMapping(typeName = "Department", field = "employeeCount")
    public int employeeCount(Department department) {
        return department.getEmployees() == null ? 0 : department.getEmployees().size();
    }

    @SchemaMapping(typeName = "Department", field = "createdAt")
    public String createdAt(Department department) {
        return department.getCreatedAt() != null ? department.getCreatedAt().toString() : null;
    }

    @SchemaMapping(typeName = "Employee", field = "hireDate")
    public String hireDate(Employee employee) {
        return employee.getHireDate() != null ? employee.getHireDate().toString() : null;
    }


    @MutationMapping
    public Department createDepartment(@Argument Map<String, Object> input) {
        return departmentService.create(
                str(input, "name"), str(input, "code"),
                str(input, "location"), dbl(input, "budget"));
    }

    @MutationMapping
    public Department updateDepartment(@Argument Long id,
                                       @Argument Map<String, Object> input) {
        return departmentService.update(
                id, str(input, "name"), str(input, "code"),
                str(input, "location"), dbl(input, "budget"));
    }

    @MutationMapping
    public Boolean deleteDepartment(@Argument Long id) {
        return departmentService.delete(id);
    }


    @MutationMapping
    public Employee createEmployee(@Argument Map<String, Object> input) {
        return employeeService.create(
                str(input, "firstName"), str(input, "lastName"),
                str(input, "email"),     str(input, "phone"),
                dbl(input, "salary"),    str(input, "hireDate"),
                str(input, "jobTitle"),  longVal(input, "departmentId"));
    }

    @MutationMapping
    public Employee updateEmployee(@Argument Long id,
                                   @Argument Map<String, Object> input) {
        return employeeService.update(
                id,
                str(input, "firstName"), str(input, "lastName"),
                str(input, "email"),     str(input, "phone"),
                dbl(input, "salary"),    str(input, "hireDate"),
                str(input, "jobTitle"),  longVal(input, "departmentId"));
    }

    @MutationMapping
    public Boolean deleteEmployee(@Argument Long id) {
        return employeeService.delete(id);
    }


    @MutationMapping
    public EmployeeProfile createEmployeeProfile(@Argument Map<String, Object> input) {
        return profileService.create(
                longVal(input, "employeeId"),
                str(input, "bio"),         str(input, "skills"),
                str(input, "linkedinUrl"), str(input, "githubUrl"),
                str(input, "address"),     str(input, "city"),
                str(input, "country"));
    }

    @MutationMapping
    public EmployeeProfile updateEmployeeProfile(@Argument Long employeeId,
                                                 @Argument Map<String, Object> input) {
        return profileService.update(
                employeeId,
                str(input, "bio"),         str(input, "skills"),
                str(input, "linkedinUrl"), str(input, "githubUrl"),
                str(input, "address"),     str(input, "city"),
                str(input, "country"));
    }


    private static String str(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? null : v.toString();
    }

    private static Double dbl(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? null : ((Number) v).doubleValue();
    }

    private static Long longVal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? null : Long.valueOf(v.toString());
    }
}
