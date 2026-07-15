package com.binovators.Binovators;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;

    public MessageController(MessageService messageService,
                             MessageRepository messageRepository,
                             UserRepository userRepository,
                             AnimalRepository animalRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
    }

    private record MessageThread(Long animalId, Long partnerId, String animalName, String partnerName, String latestText, java.time.LocalDateTime latestSentAt) {}

    @GetMapping("/messages/inbox")
    public String inbox(Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        List<Message> incoming = messageRepository.findByReceiver(currentUser);
        List<Message> outgoing = messageRepository.findBySender(currentUser);

        var threadMap = incoming.stream()
                .collect(Collectors.toMap(
                        msg -> List.of(msg.getAnimal().getId(), msg.getSender().getId()),
                        msg -> msg,
                        (msg1, msg2) -> msg1.getSentAt().isAfter(msg2.getSentAt()) ? msg1 : msg2
                ));

        outgoing.stream()
                .collect(Collectors.toMap(
                        msg -> List.of(msg.getAnimal().getId(), msg.getReceiver().getId()),
                        msg -> msg,
                        (msg1, msg2) -> msg1.getSentAt().isAfter(msg2.getSentAt()) ? msg1 : msg2
                ))
                .forEach((key, msg) -> threadMap.merge(key, msg,
                        (msg1, msg2) -> msg1.getSentAt().isAfter(msg2.getSentAt()) ? msg1 : msg2));

        List<MessageThread> threads = threadMap.values().stream()
                .map(msg -> {
                    User partner = msg.getSender().getId().equals(currentUser.getId()) ? msg.getReceiver() : msg.getSender();
                    return new MessageThread(
                            msg.getAnimal().getId(),
                            partner.getId(),
                            msg.getAnimal().getName(),
                            partner.getName(),
                            msg.getContent(),
                            msg.getSentAt()
                    );
                })
                .sorted((a, b) -> b.latestSentAt().compareTo(a.latestSentAt()))
                .collect(Collectors.toList());

        model.addAttribute("threads", threads);
        model.addAttribute("currentUser", currentUser);

        return "Inbox";
    }

    @GetMapping("/messages/thread/{animalId}/{partnerId}")
    public String chatThread(@PathVariable Long animalId,
                             @PathVariable Long partnerId,
                             Model model,
                             Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow();

        User partner = userRepository.findById(partnerId)
                .orElseThrow();

        List<Message> chatHistory = messageRepository.findChatHistory(animal, currentUser, partner);

        model.addAttribute("animal", animal);
        model.addAttribute("partner", partner);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("chatHistory", chatHistory);

        return "chat-thread";
    }

    @PostMapping("/messages/send")
    public String sendMessage(@RequestParam Long animalId,
                              @RequestParam Long receiverId,
                              @RequestParam String content,
                              @RequestParam(required = false) String redirect,
                              Principal principal) {

        messageService.sendMessage(animalId, receiverId, content, principal.getName());

        if (redirect != null && !redirect.isBlank()) {
            return "redirect:" + redirect;
        }

        return "redirect:/messages/inbox";
    }
}
