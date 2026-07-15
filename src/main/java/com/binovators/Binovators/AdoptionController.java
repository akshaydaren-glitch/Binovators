package com.binovators.Binovators;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/adoption")
public class AdoptionController {

    private final AdoptionService adoptionService;
    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;

    public AdoptionController(AdoptionService adoptionService,
                              AdoptionRepository adoptionRepository,
                              UserRepository userRepository) {
        this.adoptionService = adoptionService;
        this.adoptionRepository = adoptionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/apply/{animalId}")
    public String apply(@PathVariable Long animalId,
                        @RequestParam(required = false) String notes,
                        Principal principal) {

        adoptionService.apply(animalId, notes == null ? "" : notes, principal.getName());
        return "redirect:/adoption/my-applications";
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

    @PostMapping("/review/{applicationId}")
    public String reviewApplication(@PathVariable Long applicationId,
                                    @RequestParam String action,
                                    Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        AdoptionApplication application = adoptionRepository.findById(applicationId)
                .orElseThrow();

        if (application.getAnimal() == null || application.getAnimal().getUser() == null
                || !application.getAnimal().getUser().getId().equals(currentUser.getId())) {
            return "redirect:/adoption/my-applications";
        }

        if ("APPROVE".equalsIgnoreCase(action)) {
            application.setStatus("APPROVED");
            application.getAnimal().setStatus("ADOPTED");
            adoptionRepository.save(application);
        } else if ("REJECT".equalsIgnoreCase(action)) {
            application.setStatus("REJECTED");
            adoptionRepository.save(application);
        }

        return "redirect:/adoption/my-applications";
    }
}
