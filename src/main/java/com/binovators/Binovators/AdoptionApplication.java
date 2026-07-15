package com.binovators.Binovators;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AdoptionApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    private String notes;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public User getApplicant() {
        return applicant;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
