package com.binovators.Binovators;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    // CREATE
    @PostMapping
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalRepository.save(animal);
    }

    // READ ALL
    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Animal getAnimalById(@PathVariable Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Animal updateAnimal(@PathVariable Long id, @RequestBody Animal updatedAnimal) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        animal.setName(updatedAnimal.getName());
        animal.setType(updatedAnimal.getType());
        animal.setDescription(updatedAnimal.getDescription());
        animal.setStatus(updatedAnimal.getStatus());

        return animalRepository.save(animal);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteAnimal(@PathVariable Long id) {
        animalRepository.deleteById(id);
        return "Animal deleted with id " + id;
    }
}