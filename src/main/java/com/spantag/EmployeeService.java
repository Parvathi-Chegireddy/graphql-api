package com.spantag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository   employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository   = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findByIdWithDetails(id);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public Employee create(String firstName, String lastName, String email,
                           String phone, Double salary, String hireDate,
                           String jobTitle, Long departmentId) {
        if (employeeRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));

        Employee emp = Employee.builder()
                .firstName(firstName).lastName(lastName)
                .email(email).phone(phone)
                .salary(salary)
                .hireDate(hireDate != null ? LocalDate.parse(hireDate) : null)
                .jobTitle(jobTitle)
                .department(dept)
                .build();
        Employee saved = employeeRepository.save(emp);
        log.info("Created employee id={} email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    public Employee update(Long id, String firstName, String lastName, String email,
                           String phone, Double salary, String hireDate,
                           String jobTitle, Long departmentId) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));

        if (firstName    != null) emp.setFirstName(firstName);
        if (lastName     != null) emp.setLastName(lastName);
        if (email        != null) emp.setEmail(email);
        if (phone        != null) emp.setPhone(phone);
        if (salary       != null) emp.setSalary(salary);
        if (hireDate     != null) emp.setHireDate(LocalDate.parse(hireDate));
        if (jobTitle     != null) emp.setJobTitle(jobTitle);
        if (departmentId != null) {
            Department dept = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));
            emp.setDepartment(dept);
        }
        return employeeRepository.save(emp);
    }

    public boolean delete(Long id) {
        if (!employeeRepository.existsById(id)) return false;
        employeeRepository.deleteById(id);
        log.info("Deleted employee id={}", id);
        return true;
    }
}