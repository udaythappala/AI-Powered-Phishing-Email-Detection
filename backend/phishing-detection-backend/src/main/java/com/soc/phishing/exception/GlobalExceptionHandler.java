package com.soc.phishing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource not found
    @ExceptionHandler(
            ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handleNotFound(
            ResourceNotFoundException exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // Validation errors
    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage())
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // General errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>>
    handleGeneralException(
            Exception exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                "An unexpected error occurred");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}