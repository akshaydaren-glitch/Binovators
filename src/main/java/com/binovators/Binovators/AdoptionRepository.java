package com.binovators.Binovators;

import com.binovators.Binovators.AdoptionApplication;
import com.binovators.Binovators.Animal;
import com.binovators.Binovators.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface AdoptionRepository extends JpaRepository<AdoptionApplication, Long> {

    List<AdoptionApplication> findByAnimal(Animal animal);

    List<AdoptionApplication> findByApplicant(User user);

    List<AdoptionApplication> findByAnimalAndApplicant(Animal animal, User applicant);

    @Modifying
    @Transactional
    @Query("DELETE FROM AdoptionApplication a WHERE a.animal = :animal")
    void deleteByAnimal(@Param("animal") Animal animal);
}