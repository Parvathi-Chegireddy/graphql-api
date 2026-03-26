package com.spantag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAllWithEmployees();
    }

    @Transactional(readOnly = true)
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department create(String name, String code, String location, Double budget) {
        if (departmentRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Department code already exists: " + code);
        }
        Department dept = Department.builder()
                .name(name).code(code)
                .location(location).budget(budget)
                .build();
        Department saved = departmentRepository.save(dept);
        log.info("Created department id={} code={}", saved.getId(), saved.getCode());
        return saved;
    }

    public Department update(Long id, String name, String code, String location, Double budget) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        if (name     != null) dept.setName(name);
        if (code     != null) dept.setCode(code);
        if (location != null) dept.setLocation(location);
        if (budget   != null) dept.setBudget(budget);
        return departmentRepository.save(dept);
    }

    public boolean delete(Long id) {
        if (!departmentRepository.existsById(id)) return false;
        departmentRepository.deleteById(id);
        log.info("Deleted department id={}", id);
        return true;
    }
}