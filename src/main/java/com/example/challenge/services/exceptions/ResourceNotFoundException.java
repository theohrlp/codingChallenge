package com.example.challenge.services.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, UUID resourceId) {
        super(resourceType + ": '" + resourceId + "' not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
