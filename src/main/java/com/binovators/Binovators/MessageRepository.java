package com.binovators.Binovators;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByAnimal(Animal animal);
    List<Message> findByReceiver(User receiver);
    List<Message> findBySender(User sender);

    @Query("SELECT m FROM Message m WHERE m.animal = :animal AND " +
           "((m.sender = :currentUser AND m.receiver = :partner) OR (m.sender = :partner AND m.receiver = :currentUser)) " +
           "ORDER BY m.sentAt ASC")
    List<Message> findChatHistory(@Param("animal") Animal animal,
                                  @Param("currentUser") User currentUser,
                                  @Param("partner") User partner);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.animal = :animal")
    void deleteByAnimal(@Param("animal") Animal animal);
}
