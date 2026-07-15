package com.binovators.Binovators;

import org.springframework.stereotype.Service;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AdoptionService(AdoptionRepository adoptionRepository,
                           AnimalRepository animalRepository,
                           UserRepository userRepository) {
        this.adoptionRepository = adoptionRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    public void apply(Long animalId, String notes, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow();

        AdoptionValidator.validateAdoption(user, animal);

        AdoptionApplication app = new AdoptionApplication();
        app.setAnimal(animal);
        app.setApplicant(user);
        app.setNotes(notes);
        app.setStatus("PENDING");

        adoptionRepository.save(app);
    }
}