package com.example.challenge.services.exceptions;

public class InvalidMatchIdGiven extends RuntimeException {
    public InvalidMatchIdGiven(String message) {
        super(message);
    }
}
