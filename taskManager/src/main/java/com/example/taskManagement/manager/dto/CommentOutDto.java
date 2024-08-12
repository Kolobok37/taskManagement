package com.example.taskManagement.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentOutDto {
    private Long id;
    private Long taskId;
    private String text;
    private LocalDateTime creationDate;
    private Long userId;
    private Long taskDto;
}
