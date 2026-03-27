package com.spantag;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
    name = "employee_profile",
    indexes = {
        @Index(name = "idx_profile_emp_id", columnList = "employee_id", unique = true)
    }
)
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    @SequenceGenerator(name = "profile_seq", sequenceName = "employee_profile_id_seq",
                       allocationSize = 1)
    private Long id;

    @Column(length = 2000)
    private String bio;

    @Column(length = 1000)
    private String skills;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    public EmployeeProfile() {}

    /** Full constructor — used by the inner Builder. */
    public EmployeeProfile(Long id, String bio, String skills,
                           String linkedinUrl, String githubUrl,
                           String address, String city, String country,
                           Employee employee) {
        this.id          = id;
        this.bio         = bio;
        this.skills      = skills;
        this.linkedinUrl = linkedinUrl;
        this.githubUrl   = githubUrl;
        this.address     = address;
        this.city        = city;
        this.country     = country;
        this.employee    = employee;
    }

    // ── Getters ────────────────────────────────────────────────────

    public Long     getId()          { return id;          }
    public String   getBio()         { return bio;         }
    public String   getSkills()      { return skills;      }
    public String   getLinkedinUrl() { return linkedinUrl; }
    public String   getGithubUrl()   { return githubUrl;   }
    public String   getAddress()     { return address;     }
    public String   getCity()        { return city;        }
    public String   getCountry()     { return country;     }
    public Employee getEmployee()    { return employee;    }

    // ── Setters ────────────────────────────────────────────────────

    public void setId(Long id)                    { this.id          = id;          }
    public void setBio(String bio)                { this.bio         = bio;         }
    public void setSkills(String skills)          { this.skills      = skills;      }
    public void setLinkedinUrl(String linkedinUrl){ this.linkedinUrl = linkedinUrl; }
    public void setGithubUrl(String githubUrl)    { this.githubUrl   = githubUrl;   }
    public void setAddress(String address)        { this.address     = address;     }
    public void setCity(String city)              { this.city        = city;        }
    public void setCountry(String country)        { this.country     = country;     }
    public void setEmployee(Employee employee)    { this.employee    = employee;    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeProfile)) return false;
        EmployeeProfile other = (EmployeeProfile) o;
        return Objects.equals(id,          other.id)
            && Objects.equals(bio,         other.bio)
            && Objects.equals(skills,      other.skills)
            && Objects.equals(linkedinUrl, other.linkedinUrl)
            && Objects.equals(githubUrl,   other.githubUrl)
            && Objects.equals(address,     other.address)
            && Objects.equals(city,        other.city)
            && Objects.equals(country,     other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bio, skills, linkedinUrl,
                            githubUrl, address, city, country);
    }


    @Override
    public String toString() {
        return "EmployeeProfile{" +
               "id="             + id          +
               ", bio='"         + bio         + '\'' +
               ", skills='"      + skills      + '\'' +
               ", linkedinUrl='" + linkedinUrl + '\'' +
               ", githubUrl='"   + githubUrl   + '\'' +
               ", address='"     + address     + '\'' +
               ", city='"        + city        + '\'' +
               ", country='"     + country     + '\'' +
               '}';
    }



    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long     id;
        private String   bio;
        private String   skills;
        private String   linkedinUrl;
        private String   githubUrl;
        private String   address;
        private String   city;
        private String   country;
        private Employee employee;

        private Builder() {}

        public Builder id(Long id)                    { this.id          = id;          return this; }
        public Builder bio(String bio)                { this.bio         = bio;         return this; }
        public Builder skills(String skills)          { this.skills      = skills;      return this; }
        public Builder linkedinUrl(String linkedinUrl){ this.linkedinUrl = linkedinUrl; return this; }
        public Builder githubUrl(String githubUrl)    { this.githubUrl   = githubUrl;   return this; }
        public Builder address(String address)        { this.address     = address;     return this; }
        public Builder city(String city)              { this.city        = city;        return this; }
        public Builder country(String country)        { this.country     = country;     return this; }
        public Builder employee(Employee employee)    { this.employee    = employee;    return this; }

        public EmployeeProfile build() {
            return new EmployeeProfile(id, bio, skills, linkedinUrl, githubUrl,
                                       address, city, country, employee);
        }
    }
}
