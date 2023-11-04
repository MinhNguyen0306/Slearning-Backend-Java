package com.example.Slearning.Backend.Java.exceptions;

public class DuplicateException extends RuntimeException {
    private String objectName;
    private String field;
    private String value;

    public DuplicateException(String objectName, String field, String value) {
        super(String.format("%s can't duplicate on %s field with value %s", objectName, field, value));
        this.objectName = objectName;
        this.field = field;
        this.value = value;
    }
}
