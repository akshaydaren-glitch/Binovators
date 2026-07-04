package com.binovators.Binovators;



import com.binovators.Binovators.*;
import org.springframework.stereotype.*;

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

    public void sendMessage(Long animalId, String content, String senderUsername) {

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow();

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow();

        User receiver = animal.getUser();

        MessageValidator.validateMessage(sender, receiver);

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setAnimal(animal);
        msg.setContent(content);

        messageRepository.save(msg);
    }
}
