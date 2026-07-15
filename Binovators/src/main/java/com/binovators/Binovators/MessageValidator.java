package com.binovators.Binovators;

public class MessageValidator {

    public static void validateMessage(User sender, User receiver) {

        if (receiver == null) {
            throw new IllegalStateException("Animal has no owner");
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalStateException("Cannot message yourself");
        }
    }
}