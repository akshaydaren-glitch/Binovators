package com.binovators.Binovators;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          AnimalRepository animalRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    public void sendMessage(Long animalId, Long receiverId, String content, String senderUsername) {

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow();

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow();

        User receiver = userRepository.findById(receiverId)
                .orElseThrow();

        User animalOwner = animal.getUser();
        if (animalOwner == null) {
            throw new IllegalStateException("Animal has no owner");
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalStateException("Cannot message yourself");
        }

        boolean senderIsOwner = sender.getId().equals(animalOwner.getId());
        boolean receiverIsOwner = receiver.getId().equals(animalOwner.getId());

        if (!senderIsOwner && !receiverIsOwner) {
            throw new IllegalStateException("Invalid recipient for this animal");
        }

        MessageValidator.validateMessage(sender, receiver);

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setAnimal(animal);
        msg.setContent(content);

        messageRepository.save(msg);
    }
}
