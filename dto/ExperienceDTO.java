package com.Siw.personalProject.dto;

import java.time.LocalDate;

public class ExperienceDTO {

    private Long id;
    private String role;
    private String company;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public ExperienceDTO() {}

    // GETTER e SETTER
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}