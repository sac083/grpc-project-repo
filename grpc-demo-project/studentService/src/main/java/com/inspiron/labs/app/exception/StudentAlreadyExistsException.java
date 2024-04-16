package com.inspiron.labs.app.exception;

public class StudentAlreadyExistsException extends  RuntimeException {
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
