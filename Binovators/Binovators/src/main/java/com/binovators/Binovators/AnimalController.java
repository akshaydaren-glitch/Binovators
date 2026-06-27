package com.binovators.Binovators;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    // CREATE
    @PostMapping
    public String createAnimal(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam String status) {

        Animal animal = new Animal();
        animal.setName(name);
        animal.setType(type);
        animal.setDescription(description);
        animal.setStatus(status);

        animalRepository.save(animal);

        return "redirect:/animals/page";
    }

    // SHOW PAGE
    @GetMapping("/page")
    public String showAnimals(Model model) {
        model.addAttribute("animals", animalRepository.findAll());
        return "animals";
    }

    

    // DELETE (simple HTML link version)
    @GetMapping("/delete/{id}")
    public String deleteAnimal(@PathVariable Long id) {
        animalRepository.deleteById(id);
        return "redirect:/animals/page";
    }

    @GetMapping("/edit/{id}")
public String showEditPage(@PathVariable Long id, Model model) {

    Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Animal not found"));

    model.addAttribute("animal", animal);

    return "edit-animal";
}

@PostMapping("/update/{id}")
public String updateAnimal(@PathVariable Long id,
                           @RequestParam String name,
                           @RequestParam String type,
                           @RequestParam String description,
                           @RequestParam String status) {

    Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Animal not found"));

    animal.setName(name);
    animal.setType(type);
    animal.setDescription(description);
    animal.setStatus(status);

    animalRepository.save(animal);

    return "redirect:/animals/page";
}
}