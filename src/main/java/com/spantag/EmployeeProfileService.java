package com.spantag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmployeeProfileService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeProfileService.class);

    private final EmployeeProfileRepository profileRepository;
    private final EmployeeRepository        employeeRepository;

    public EmployeeProfileService(EmployeeProfileRepository profileRepository,
                                  EmployeeRepository employeeRepository) {
        this.profileRepository  = profileRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeProfile> findByEmployeeId(Long employeeId) {
        return profileRepository.findByEmployeeId(employeeId);
    }

    public EmployeeProfile create(Long employeeId, String bio, String skills,
                                  String linkedinUrl, String githubUrl,
                                  String address, String city, String country) {
        if (profileRepository.existsByEmployeeId(employeeId)) {
            throw new IllegalStateException("Profile already exists for employee: " + employeeId);
        }
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        EmployeeProfile profile = EmployeeProfile.builder()
                .employee(emp).bio(bio).skills(skills)
                .linkedinUrl(linkedinUrl).githubUrl(githubUrl)
                .address(address).city(city).country(country)
                .build();
        EmployeeProfile saved = profileRepository.save(profile);
        log.info("Created profile id={} for employee id={}", saved.getId(), employeeId);
        return saved;
    }

    public EmployeeProfile update(Long employeeId, String bio, String skills,
                                  String linkedinUrl, String githubUrl,
                                  String address, String city, String country) {
        EmployeeProfile profile = profileRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Profile not found for employee: " + employeeId));

        if (bio         != null) profile.setBio(bio);
        if (skills      != null) profile.setSkills(skills);
        if (linkedinUrl != null) profile.setLinkedinUrl(linkedinUrl);
        if (githubUrl   != null) profile.setGithubUrl(githubUrl);
        if (address     != null) profile.setAddress(address);
        if (city        != null) profile.setCity(city);
        if (country     != null) profile.setCountry(country);

        return profileRepository.save(profile);
    }
}