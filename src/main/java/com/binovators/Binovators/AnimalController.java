package com.binovators.Binovators;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Controller
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private final MessageRepository messageRepository;

    public AnimalController(
        AnimalRepository animalRepository,
        UserRepository userRepository,
        MessageRepository messageRepository,
        AdoptionRepository adoptionRepository) {

        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.adoptionRepository = adoptionRepository;
    }

    private User getCurrentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userRepository.findByUsername(principal.getName()).orElse(null);
    }

    private boolean isAdmin(User user) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        String role = user.getRole().trim();
        return "ADMIN".equalsIgnoreCase(role) || "ROLE_ADMIN".equalsIgnoreCase(role);
    }

    // CREATE
    @PostMapping
public String createAnimal(
        @RequestParam String name,
        @RequestParam String type,
        @RequestParam String description,
        @RequestParam String status,
        @RequestParam MultipartFile image,
        Principal principal) throws IOException{

    User user = userRepository
            .findByUsername(principal.getName())
            .orElseThrow();

    Animal animal = new Animal();

    animal.setName(name);
    animal.setType(type);
    animal.setDescription(description);
    animal.setStatus(status);
    animal.setImage(image.getBytes());

    animal.setUser(user);

    animalRepository.save(animal);

    return "redirect:/animals/page";
}

    // SHOW PAGE
    @GetMapping("/animals")
    public String showAllAnimals(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        List<Animal> animals = animalRepository.findAll();
        List<Animal> safeAnimals = animals == null ? List.of() :
                animals.stream()
                        .filter(Objects::nonNull)
                        .filter(animal -> currentUser == null || animal.getUser() == null || !animal.getUser().getId().equals(currentUser.getId()))
                        .collect(Collectors.toList());
        model.addAttribute("animals", safeAnimals);
        model.addAttribute("currentUser", currentUser);

        return "animals";
    }

    @GetMapping("/page")
    public String showAnimals(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        List<Animal> animals = animalRepository.findAll();
        List<Animal> safeAnimals = animals == null ? List.of() :
                animals.stream()
                        .filter(Objects::nonNull)
                        .filter(animal -> currentUser == null || animal.getUser() == null || !animal.getUser().getId().equals(currentUser.getId()))
                        .collect(Collectors.toList());
        model.addAttribute("animals", safeAnimals);
        model.addAttribute("currentUser", currentUser);
        return "animals";
    }
    
        @GetMapping("/my")
    public String myAnimals(Model model,
                            Principal principal) {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        List<Animal> animalsByUser = animalRepository.findByUser(user);
        List<Animal> safeAnimalsByUser = animalsByUser == null ? List.of() :
            animalsByUser.stream().filter(Objects::nonNull).collect(Collectors.toList());

        model.addAttribute(
            "animals",
            safeAnimalsByUser
        );
        model.addAttribute("currentUser", user);

        return "my-animals";
    }
    

    // DELETE (simple HTML link version)
    @Transactional
    @GetMapping("/delete/{id}")
    public String deleteAnimal(@PathVariable Long id, Principal principal) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow();

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        boolean isOwner = animal.getUser() != null &&
                animal.getUser().getUsername().equals(currentUser.getUsername());

        boolean isAdmin = isAdmin(currentUser);

        if (!isOwner && !isAdmin) {
            return "redirect:/animals/page?error=notAuthorized";
        }

        try {
            // ✅ IMPORTANT: delete dependencies first
            messageRepository.deleteByAnimal(animal);
            adoptionRepository.deleteByAnimal(animal);
            animalRepository.delete(animal);
        } catch (DataIntegrityViolationException ex) {
            return "redirect:/animals/page?error=deleteFailed";
        }

        return "redirect:/animals/page";
    }

        @PostMapping("/update/{id}")
    public String updateAnimal(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam String type,
                            @RequestParam String description,
                            @RequestParam String status,
                            Principal principal) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow();

        boolean isOwner = animal.getUser().getUsername()
                .equals(principal.getName());

        if (!isOwner) {
            return "redirect:/animals/page";
        }

        animal.setName(name);
        animal.setType(type);
        animal.setDescription(description);
        animal.setStatus(status);

        animalRepository.save(animal);

        return "redirect:/animals/my";
    }
    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

    Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Animal not found"));

    return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(animal.getImage());
}

}