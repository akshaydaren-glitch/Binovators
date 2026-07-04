package com.binovators.Binovators;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository,
                             AnimalRepository animalRepository,
                             UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // INBOX
    // =========================
    @GetMapping("/inbox")
    public String inbox(Model model, Principal principal) {

         User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        List<Message> messages = messageRepository.findAllByUser(currentUser);
        List<Message> groupedThreads = groupConversationThreads(messages, currentUser);

        model.addAttribute("messages", groupedThreads);
        model.addAttribute("currentUser", currentUser);

        return "Inbox";
    }

    // =========================
    // THREAD
    // =========================
    @GetMapping("/thread/{animalId}/{partnerId}")
    public String thread(@PathVariable Long animalId,
                         @PathVariable Long partnerId,
                         Model model,
                         Principal principal) {

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        Animal animal = animalRepository.findById(animalId)
                .orElse(null);

        User partner = userRepository.findById(partnerId)
                .orElse(null);

        if (animal == null || partner == null) {
            return "redirect:/messages/inbox";
        }

        List<Message> chat = messageRepository.findChatHistory(
                animalId,
                currentUser.getId(),
                partnerId
        );

        model.addAttribute("chatHistory", chat);
        model.addAttribute("animal", animal);
        model.addAttribute("partner", partner);
        model.addAttribute("currentUser", currentUser);

        return "chat-thread";
    }

    private List<Message> groupConversationThreads(List<Message> messages, User currentUser) {
        Map<String, Message> latestByThread = new LinkedHashMap<>();

        for (Message message : messages) {
            Long animalId = message.getAnimal() != null ? message.getAnimal().getId() : null;
            Long senderId = message.getSender() != null ? message.getSender().getId() : null;
            Long receiverId = message.getReceiver() != null ? message.getReceiver().getId() : null;

            if (animalId == null || senderId == null || receiverId == null) {
                continue;
            }

            Long first = Math.min(senderId, receiverId);
            Long second = Math.max(senderId, receiverId);
            String key = animalId + "::" + first + "::" + second;

            Message existing = latestByThread.get(key);
            if (existing == null || (message.getTimestamp() != null && (existing.getTimestamp() == null || message.getTimestamp().isAfter(existing.getTimestamp())))) {
                latestByThread.put(key, message);
            }
        }

        List<Message> grouped = new ArrayList<>(latestByThread.values());
        grouped.sort(Comparator.comparing(Message::getTimestamp, Comparator.nullsLast(Comparator.reverseOrder())));
        return grouped;
    }

    // =========================
    // SEND MESSAGE
    // =========================
   @PostMapping("/send")
public String sendMessage(@RequestParam Long animalId,
                          @RequestParam Long receiverId,
                          @RequestParam String content,
                          Principal principal) {

    User sender = userRepository.findByUsername(principal.getName())
            .orElseThrow();

    Animal animal = animalRepository.findById(animalId)
            .orElseThrow();

    User receiver = userRepository.findById(receiverId)
            .orElseThrow();

    if (receiver == null) {
        return "redirect:/animals?error=no_receiver";
    }

    // prevent self-message
    if (sender.getId().equals(receiver.getId())) {
        return "redirect:/animals?error=self_message";
    }

    Message msg = new Message();
    msg.setSender(sender);
    msg.setReceiver(receiver);
    msg.setAnimal(animal);
    msg.setContent(content);

    messageRepository.save(msg);

    return "redirect:/messages/thread/" + animalId + "/" + receiver.getId();
}
}
