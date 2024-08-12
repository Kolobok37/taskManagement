package com.example.taskManagement.manager.exception;

public class DuplicateDataException extends ValidationDataException {
    public DuplicateDataException(String message) {
        super(message);
    }
}
