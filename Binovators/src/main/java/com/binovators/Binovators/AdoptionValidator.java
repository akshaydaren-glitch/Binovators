package com.binovators.Binovators;

public class AdoptionValidator {

    public static void validateAdoption(User user, Animal animal) {

        if (animal.getUser() == null) {
            throw new IllegalStateException("Animal has no owner");
        }

        if (animal.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Cannot adopt your own animal");
        }
    }
}