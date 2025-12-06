package com.Siw.personalProject.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "experiences")
public class ExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;     
    private String company;      
    private LocalDate startDate; 
    private LocalDate endDate;   
    private String description;  
}
