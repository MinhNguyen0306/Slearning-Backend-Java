package com.example.Slearning.Backend.Java.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private long value;
    private String message;

    public ResourceNotFoundException() {
        super();
        String.format("Not found %s with %d", message, value);
    }
}
