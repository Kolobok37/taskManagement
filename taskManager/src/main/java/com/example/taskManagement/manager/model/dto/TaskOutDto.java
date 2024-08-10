package com.example.taskManagement.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TaskOutDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    public UserDto creator;
    public UserDto performer;
    public List<CommentOutDto> comments;
}