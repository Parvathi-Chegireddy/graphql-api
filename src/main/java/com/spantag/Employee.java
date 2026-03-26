package com.spantag;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Employee entity.
 *
 * Relationships:
 *   MANY-TO-ONE  →  Employee belongs to one Department.
 *   ONE-TO-ONE   →  Employee optionally owns one EmployeeProfile.
 */
@Entity
@Table(
    name = "employee",
    indexes = {
        @Index(name = "idx_emp_email",   columnList = "email",         unique = true),
        @Index(name = "idx_emp_dept_id", columnList = "department_id")
    }
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "employee_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private Double salary;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "job_title")
    private String jobTitle;

    // ── Many-to-One: many Employees → one Department ──────────────
    // Excluded from toString / equals / hashCode to avoid lazy-load & cycles
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // ── One-to-One: one Employee → one EmployeeProfile ────────────
    // Excluded from toString / equals / hashCode to avoid lazy-load & cycles
    @OneToOne(
        mappedBy      = "employee",
        cascade       = CascadeType.ALL,
        fetch         = FetchType.LAZY,
        optional      = true,
        orphanRemoval = true
    )
    private EmployeeProfile profile;

    // ── Constructors ───────────────────────────────────────────────

    /** Required by JPA. */
    public Employee() {}

    /** Full constructor — used by the inner Builder. */
    public Employee(Long id, String firstName, String lastName, String email,
                    String phone, Double salary, LocalDate hireDate,
                    String jobTitle, Department department, EmployeeProfile profile) {
        this.id         = id;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
        this.phone      = phone;
        this.salary     = salary;
        this.hireDate   = hireDate;
        this.jobTitle   = jobTitle;
        this.department = department;
        this.profile    = profile;
    }

    // ── Getters ────────────────────────────────────────────────────

    public Long            getId()         { return id;         }
    public String          getFirstName()  { return firstName;  }
    public String          getLastName()   { return lastName;   }
    public String          getEmail()      { return email;      }
    public String          getPhone()      { return phone;      }
    public Double          getSalary()     { return salary;     }
    public LocalDate       getHireDate()   { return hireDate;   }
    public String          getJobTitle()   { return jobTitle;   }
    public Department      getDepartment() { return department; }
    public EmployeeProfile getProfile()    { return profile;    }

    // ── Setters ────────────────────────────────────────────────────

    public void setId(Long id)                       { this.id         = id;         }
    public void setFirstName(String firstName)       { this.firstName  = firstName;  }
    public void setLastName(String lastName)         { this.lastName   = lastName;   }
    public void setEmail(String email)               { this.email      = email;      }
    public void setPhone(String phone)               { this.phone      = phone;      }
    public void setSalary(Double salary)             { this.salary     = salary;     }
    public void setHireDate(LocalDate hireDate)      { this.hireDate   = hireDate;   }
    public void setJobTitle(String jobTitle)         { this.jobTitle   = jobTitle;   }
    public void setDepartment(Department department) { this.department = department; }
    public void setProfile(EmployeeProfile profile)  { this.profile    = profile;    }

    // ── equals & hashCode (scalar fields only; excludes lazy relations) ─

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee other = (Employee) o;
        return Objects.equals(id,        other.id)
            && Objects.equals(firstName, other.firstName)
            && Objects.equals(lastName,  other.lastName)
            && Objects.equals(email,     other.email)
            && Objects.equals(phone,     other.phone)
            && Objects.equals(salary,    other.salary)
            && Objects.equals(hireDate,  other.hireDate)
            && Objects.equals(jobTitle,  other.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email,
                            phone, salary, hireDate, jobTitle);
    }

    // ── toString (excludes lazy relations to prevent N+1 / cycles) ─

    @Override
    public String toString() {
        return "Employee{" +
               "id="           + id        +
               ", firstName='" + firstName + '\'' +
               ", lastName='"  + lastName  + '\'' +
               ", email='"     + email     + '\'' +
               ", phone='"     + phone     + '\'' +
               ", salary="     + salary    +
               ", hireDate="   + hireDate  +
               ", jobTitle='"  + jobTitle  + '\'' +
               '}';
    }

    // ── Builder ────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long            id;
        private String          firstName;
        private String          lastName;
        private String          email;
        private String          phone;
        private Double          salary;
        private LocalDate       hireDate;
        private String          jobTitle;
        private Department      department;
        private EmployeeProfile profile;

        private Builder() {}

        public Builder id(Long id)                       { this.id         = id;         return this; }
        public Builder firstName(String firstName)       { this.firstName  = firstName;  return this; }
        public Builder lastName(String lastName)         { this.lastName   = lastName;   return this; }
        public Builder email(String email)               { this.email      = email;      return this; }
        public Builder phone(String phone)               { this.phone      = phone;      return this; }
        public Builder salary(Double salary)             { this.salary     = salary;     return this; }
        public Builder hireDate(LocalDate hireDate)      { this.hireDate   = hireDate;   return this; }
        public Builder jobTitle(String jobTitle)         { this.jobTitle   = jobTitle;   return this; }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder profile(EmployeeProfile profile)  { this.profile    = profile;    return this; }

        public Employee build() {
            return new Employee(id, firstName, lastName, email,
                                phone, salary, hireDate, jobTitle,
                                department, profile);
        }
    }
}