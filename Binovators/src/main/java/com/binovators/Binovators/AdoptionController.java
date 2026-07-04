package com.binovators.Binovators;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/adoption")
public class AdoptionController {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public AdoptionController(AdoptionRepository adoptionRepository,
                              AnimalRepository animalRepository,
                              UserRepository userRepository) {
        this.adoptionRepository = adoptionRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-applications")
    public String myApplications(Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        List<AdoptionApplication> inbound = adoptionRepository.findAll().stream()
                .filter(app -> app.getAnimal() != null && app.getAnimal().getUser() != null)
                .filter(app -> app.getAnimal().getUser().getId().equals(currentUser.getId()))
                .toList();

        List<AdoptionApplication> outbound = adoptionRepository.findByApplicant(currentUser);

        model.addAttribute("inbound", inbound);
        model.addAttribute("outbound", outbound);

        return "applications";
    }

    @PostMapping("/apply/{animalId}")
    public String applyForAdoption(@PathVariable Long animalId,
                                   @RequestParam String notes,
                                   Principal principal) {

        User applicant = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow();

        if (animal.getUser() != null && animal.getUser().getId().equals(applicant.getId())) {
            return "redirect:/animals/page?error=cannot_adopt_own";
        }

        List<AdoptionApplication> existing = adoptionRepository.findByAnimalAndApplicant(animal, applicant);
        if (!existing.isEmpty()) {
            return "redirect:/animals/page?error=already_applied";
        }

        AdoptionApplication application = new AdoptionApplication();
        application.setAnimal(animal);
        application.setApplicant(applicant);
        application.setNotes(notes);
        application.setStatus("PENDING");

        adoptionRepository.save(application);

        return "redirect:/animals/page?success=adoption_sent";
    }

    @PostMapping("/review/{applicationId}")
    public String reviewApplication(@PathVariable Long applicationId,
                                    @RequestParam String action,
                                    Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        AdoptionApplication application = adoptionRepository.findById(applicationId)
                .orElseThrow();

        if (application.getAnimal() == null || application.getAnimal().getUser() == null ||
                !application.getAnimal().getUser().getId().equals(currentUser.getId())) {
            return "redirect:/adoption/my-applications?error=not_allowed";
        }

        application.setStatus(action.equals("APPROVE") ? "APPROVED" : "REJECTED");
        if (action.equals("APPROVE")) {
            Animal adoptedAnimal = application.getAnimal();
            adoptedAnimal.setStatus("ADOPTED");
            adoptedAnimal.setUser(application.getApplicant());
            animalRepository.save(adoptedAnimal);
        }
        adoptionRepository.save(application);

        return "redirect:/adoption/my-applications?success=reviewed";
    }
}