package com.example.taskManagement.manager.entities;

import lombok.Getter;

@Getter
public enum Priority {
    Low(0),
    Mid(1),
    High(2);


    private final int value;


    Priority(int value) {
        this.value = value;
    }

    public static Priority fromValue(int value) {
        for (Priority priority : Priority.values()) {
            if (priority.getValue() == value) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Не валидное значение приоритета: " + value);
    }

    public int getValue() {
        return value;
    }
}
