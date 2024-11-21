package com.example.challenge.services.exceptions;

import static java.lang.String.format;

public class MismatchedIdsException extends RuntimeException {
    private static final String MESSAGE = "Provided id in URL: %s does not match id in payload: %s";

    public MismatchedIdsException(String idPathVariable, String idPayload) {
        super(format(MESSAGE, idPathVariable, idPayload));
    }
}
