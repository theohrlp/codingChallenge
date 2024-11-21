package com.example.challenge.controllers;

import com.example.challenge.services.exceptions.InvalidMatchIdGiven;
import com.example.challenge.services.exceptions.InvalidRequestException;
import com.example.challenge.services.exceptions.MismatchedIdsException;
import com.example.challenge.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    static final String INTERNAL_SERVER_ERROR_RESPONSE_BODY = "Something went wrong!";

    // TODO add problem details for response

     //400
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex) {
        logger.error("Invalid request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MismatchedIdsException.class)
    public ResponseEntity<String> handleMismatchedIdsException(MismatchedIdsException ex) {
        logger.error("Invalid request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // Extract meaningful error message
        String errorMessage = ex.getMostSpecificCause() != null ?
                ex.getMostSpecificCause().getMessage() :
                ex.getMessage();

        // Customize error response
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Malformed request");
        errorResponse.put("message", errorMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    //422
    @ExceptionHandler(InvalidMatchIdGiven.class)
    public ResponseEntity<String> handleInvalidMatchIdGiven(InvalidMatchIdGiven ex) {
        logger.error("Could not process: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR_RESPONSE_BODY);
    }
}
