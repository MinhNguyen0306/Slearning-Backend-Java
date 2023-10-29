package com.example.Slearning.Backend.Java.exceptions;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private int intValue;
    private UUID uuidValue;
    private String stringValue;

    public ResourceNotFoundException(String resourceName, String fieldName, int intValue) {
        super(String.format("%s not found with %s: %d", resourceName, fieldName, intValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.intValue = intValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String stringValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, stringValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.stringValue = stringValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, UUID uuidValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, uuidValue.toString()));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.uuidValue = uuidValue;
    }
}
