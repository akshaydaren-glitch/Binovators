package com.binovators.Binovators;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdoptionRepository extends JpaRepository<AdoptionApplication, Long> {

    List<AdoptionApplication> findByAnimal(Animal animal);

    List<AdoptionApplication> findByApplicant(User user);

    List<AdoptionApplication> findByAnimalAndApplicant(Animal animal, User applicant);

    void deleteByAnimal(Animal animal);
}