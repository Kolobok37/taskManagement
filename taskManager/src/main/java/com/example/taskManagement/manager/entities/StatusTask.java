package com.example.taskManagement.manager.entities;

public enum StatusTask {
    Created(0),
    Working(1),
    Completed(2);

    private final int value;

    StatusTask(int value) {
        this.value = value;
    }

    public static StatusTask fromValue(int value) {
        for (StatusTask status : StatusTask.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Не валидное значение статуса: " + value);
    }

    public int getValue() {
        return value;
    }
}