package com.spantag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentId(Long departmentId);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    /** Fetch employee with its department and profile in one query. */
    @Query("""
        SELECT e FROM Employee e
        LEFT JOIN FETCH e.department
        LEFT JOIN FETCH e.profile
        WHERE e.id = :id
        """)
    Optional<Employee> findByIdWithDetails(@Param("id") Long id);

    /** Fetch all employees with department (avoids N+1). */
    @Query("""
        SELECT DISTINCT e FROM Employee e
        LEFT JOIN FETCH e.department
        LEFT JOIN FETCH e.profile
        """)
    List<Employee> findAllWithDetails();
}