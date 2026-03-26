package com.spantag;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DepartmentRepository      deptRepo;
    private final EmployeeRepository        empRepo;
    private final EmployeeProfileRepository profileRepo;

    public DataInitializer(DepartmentRepository deptRepo,
                           EmployeeRepository empRepo,
                           EmployeeProfileRepository profileRepo) {
        this.deptRepo    = deptRepo;
        this.empRepo     = empRepo;
        this.profileRepo = profileRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("═══════════════════════════════════════════");
        log.info("  Seeding PostgreSQL database …");
        log.info("═══════════════════════════════════════════");

        // ── Departments ──────────────────────────────────────────
        Department eng = deptRepo.save(dept("Engineering",      "ENG", "Bengaluru, India",  5_000_000.0));
        Department mkt = deptRepo.save(dept("Marketing",        "MKT", "Mumbai, India",     2_000_000.0));
        Department hr  = deptRepo.save(dept("Human Resources",  "HR",  "Delhi, India",      1_500_000.0));
        Department fin = deptRepo.save(dept("Finance",          "FIN", "Chennai, India",    3_000_000.0));
        Department dat = deptRepo.save(dept("Data Science",     "DS",  "Hyderabad, India",  2_500_000.0));

        // ── Employees ─────────────────────────────────────────────
        Employee parvathi = empRepo.save(emp("parvathi",   "chegireddy",  "parvathi.chegireddy@company.com",
                "+91-9999999999", 120_000.0, "2021-03-15", "Software Engineer", eng));
        Employee pradeep   = empRepo.save(emp("pradeep",     "S",    "pradeep.s@company.com",
                "+91-9007654321",  95_000.0, "2020-06-01", "Backend Developer",         eng));
        Employee carol = empRepo.save(emp("Carol",   "Williams", "carol.williams@company.com",
                "+91-9003456789",  85_000.0, "2022-01-10", "Frontend Developer",        eng));
        Employee henry = empRepo.save(emp("Henry",   "Moore",    "henry.moore@company.com",
                "+91-9005791234", 105_000.0, "2021-08-30", "DevOps Engineer",            eng));
        Employee david = empRepo.save(emp("David",   "Brown",    "david.brown@company.com",
                "+91-9009876543",  90_000.0, "2019-09-20", "Marketing Manager",          mkt));
        Employee emma  = empRepo.save(emp("Emma",    "Davis",    "emma.davis@company.com",
                "+91-9002468135",  75_000.0, "2023-02-14", "Content Strategist",         mkt));
        Employee frank = empRepo.save(emp("Frank",   "Miller",   "frank.miller@company.com",
                "+91-9001357924",  80_000.0, "2020-11-05", "HR Business Partner",        hr));
        Employee grace = empRepo.save(emp("Grace",   "Wilson",   "grace.wilson@company.com",
                "+91-9008642097", 110_000.0, "2018-07-22", "Finance Controller",         fin));
        Employee ivan  = empRepo.save(emp("Ivan",    "Chen",     "ivan.chen@company.com",
                "+91-9004567890", 130_000.0, "2022-05-18", "Lead Data Scientist",        dat));
        Employee julia = empRepo.save(emp("Julia",   "Patel",    "julia.patel@company.com",
                "+91-9006789012", 115_000.0, "2023-07-01", "ML Engineer",                dat));

        // ── Employee Profiles (One-to-One) ────────────────────────
        profileRepo.saveAll(List.of(
           /* profile(alice, "Passionate full-stack developer with 8+ years in Java and React.",
                    "Java, Spring Boot, React, GraphQL, Kubernetes, AWS",
                    "https://linkedin.com/in/alice-johnson", "https://github.com/alice-j",
                    "123 MG Road", "Bengaluru", "India"),*/
        		profile(parvathi, "Passionate java developer  in Java and React.",
                        "Java, Spring Boot, React, GraphQL",
                        "http://linkedin.com/in/chegireddy-parvathi-183376289", "https://github.com/Parvathi-Chegireddy/oauth2.git",
                        "OMR Road", "Chennai", "India"),
        		

            profile(pradeep, "sales-force,TL",
                    "salesforce",
                    "https://www.linkedin.com/in/raja-pradeep-e-6686b1212?", "https://github.com/bob-smith",
                    "perungudi", "Chennai", "India"),

            profile(carol, "Creative frontend developer building beautiful, accessible UIs.",
                    "React, TypeScript, CSS, Next.js, Figma, Storybook",
                    "https://linkedin.com/in/carol-williams", "https://github.com/carol-w",
                    "789 HSR Layout", "Bengaluru", "India"),

            profile(henry, "Cloud infrastructure engineer specialising in CI/CD and containers.",
                    "AWS, Terraform, Kubernetes, Jenkins, Prometheus, Grafana",
                    "https://linkedin.com/in/henry-moore-devops", "https://github.com/henry-m",
                    "987 Whitefield", "Bengaluru", "India"),

            profile(david, "Results-driven marketing manager with expertise in digital campaigns.",
                    "SEO, SEM, Google Analytics, HubSpot, Content Strategy",
                    "https://linkedin.com/in/david-brown-mkt", null,
                    "321 Bandra West", "Mumbai", "India"),

            profile(grace, "Seasoned finance professional specialising in corporate accounting.",
                    "SAP, Excel, Financial Modelling, Risk Management, IFRS",
                    "https://linkedin.com/in/grace-wilson-fin", null,
                    "654 Anna Nagar", "Chennai", "India"),

            profile(ivan, "Data scientist with 6+ years building ML pipelines at scale.",
                    "Python, TensorFlow, PyTorch, Spark, SQL, LLM Fine-tuning",
                    "https://linkedin.com/in/ivan-chen-ds", "https://github.com/ivan-chen",
                    "11 Jubilee Hills", "Hyderabad", "India"),

            profile(julia, "ML engineer passionate about deploying models to production.",
                    "Python, MLflow, FastAPI, Docker, Kubeflow, Hugging Face",
                    "https://linkedin.com/in/julia-patel-ml", "https://github.com/julia-p",
                    "22 Banjara Hills", "Hyderabad", "India")
        ));

        log.info("✅ Seeded {} departments, {} employees, {} profiles",
                deptRepo.count(), empRepo.count(), profileRepo.count());
        log.info("═══════════════════════════════════════════");
    }

    // ── Private factory helpers ───────────────────────────────────

    private static Department dept(String name, String code, String location, double budget) {
        return Department.builder()
                .name(name).code(code).location(location).budget(budget)
                .build();
    }

    private static Employee emp(String first, String last, String email,
                                String phone, double salary, String hireDate,
                                String jobTitle, Department dept) {
        return Employee.builder()
                .firstName(first).lastName(last)
                .email(email).phone(phone).salary(salary)
                .hireDate(LocalDate.parse(hireDate))
                .jobTitle(jobTitle).department(dept)
                .build();
    }

    private static EmployeeProfile profile(Employee emp, String bio, String skills,
                                           String linkedin, String github,
                                           String address, String city, String country) {
        return EmployeeProfile.builder()
                .employee(emp).bio(bio).skills(skills)
                .linkedinUrl(linkedin).githubUrl(github)
                .address(address).city(city).country(country)
                .build();
    }
}