package com.binovators.Binovators;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // =========================
    // CHAT THREAD (BETWEEN 2 USERS FOR 1 ANIMAL)
    // =========================
    @Query("""
        SELECT m FROM Message m
        WHERE m.animal.id = :animalId
        AND (
            (m.sender.id = :u1 AND m.receiver.id = :u2)
            OR
            (m.sender.id = :u2 AND m.receiver.id = :u1)
        )
        ORDER BY m.timestamp ASC
    """)
    List<Message> findChatHistory(
            @Param("animalId") Long animalId,
            @Param("u1") Long u1,
            @Param("u2") Long u2
    );

    // =========================
    // INBOX (ALL USER MESSAGES)
    // =========================
    @Query("SELECT m FROM Message m " +
       "JOIN FETCH m.animal " +
       "JOIN FETCH m.sender " +
       "JOIN FETCH m.receiver " +
       "WHERE m.sender = :user OR m.receiver = :user " +
       "ORDER BY m.timestamp DESC")
List<Message> findAllByUser(@Param("user") User user);

    // =========================
    // CLEANUP (IMPORTANT FOR DELETE FIX)
    // =========================
    void deleteByAnimal(Animal animal);

    List<Message> findAllByReceiver(User receiver);
    List<Message> findAllBySender(User sender);
}