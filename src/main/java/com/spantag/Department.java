package com.spantag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(
    name = "department",
    indexes = {
        @Index(name = "idx_dept_code", columnList = "code", unique = true)
    }
)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dept_seq")
    @SequenceGenerator(name = "dept_seq", sequenceName = "department_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    private String location;

    private Double budget;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    
    @OneToMany(
        mappedBy      = "department",
        cascade       = CascadeType.ALL,
        fetch         = FetchType.LAZY,
        orphanRemoval = true
    )
    private List<Employee> employees = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


    public Department() {}

    public Department(Long id, String name, String code, String location,
                      Double budget, LocalDateTime createdAt, List<Employee> employees) {
        this.id        = id;
        this.name      = name;
        this.code      = code;
        this.location  = location;
        this.budget    = budget;
        this.createdAt = createdAt;
        this.employees = employees != null ? employees : new ArrayList<>();
    }

    // ── Getters ────────────────────────────────────────────────────

    public Long           getId()        { return id;        }
    public String         getName()      { return name;      }
    public String         getCode()      { return code;      }
    public String         getLocation()  { return location;  }
    public Double         getBudget()    { return budget;    }
    public LocalDateTime  getCreatedAt() { return createdAt; }
    public List<Employee> getEmployees() { return employees; }

    // ── Setters ────────────────────────────────────────────────────

    public void setId(Long id)                       { this.id        = id;        }
    public void setName(String name)                 { this.name      = name;      }
    public void setCode(String code)                 { this.code      = code;      }
    public void setLocation(String location)         { this.location  = location;  }
    public void setBudget(Double budget)             { this.budget    = budget;    }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees != null ? employees : new ArrayList<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department other = (Department) o;
        return Objects.equals(id,        other.id)
            && Objects.equals(name,      other.name)
            && Objects.equals(code,      other.code)
            && Objects.equals(location,  other.location)
            && Objects.equals(budget,    other.budget)
            && Objects.equals(createdAt, other.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, location, budget, createdAt);
    }


    @Override
    public String toString() {
        return "Department{" +
               "id="          + id        +
               ", name='"     + name      + '\'' +
               ", code='"     + code      + '\'' +
               ", location='" + location  + '\'' +
               ", budget="    + budget    +
               ", createdAt=" + createdAt +
               '}';
    }

   

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long           id;
        private String         name;
        private String         code;
        private String         location;
        private Double         budget;
        private LocalDateTime  createdAt;
        private List<Employee> employees = new ArrayList<>();

        private Builder() {}

        public Builder id(Long id)                         { this.id        = id;        return this; }
        public Builder name(String name)                   { this.name      = name;      return this; }
        public Builder code(String code)                   { this.code      = code;      return this; }
        public Builder location(String location)           { this.location  = location;  return this; }
        public Builder budget(Double budget)               { this.budget    = budget;    return this; }
        public Builder createdAt(LocalDateTime createdAt)  { this.createdAt = createdAt; return this; }
        public Builder employees(List<Employee> employees) {
            this.employees = employees != null ? employees : new ArrayList<>();
            return this;
        }

        public Department build() {
            return new Department(id, name, code, location, budget, createdAt, employees);
        }
    }
}