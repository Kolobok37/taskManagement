package com.example.taskManagement.manager.model.dto;

import com.example.taskManagement.manager.model.enam.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskInDto {
    @Schema(description = "Загаловок задачи", example = "Тестовое задание")
    @Size(min = 5, max = 50, message = "Загаловок задачи должнен содержать от 5 до 50 символов")
    @NotBlank(message = "Загаловок задачи не может быть пустыми")
    private String title;
    @Schema(description = "Описание задачи", example = "Необходимо разработать простую систему управления задачами")
    @Size(min = 5, max = 50, message = "Описание задачи должно быть менее 500 символов")
    private String description;
    @Schema(description = "Приоритет задачи", example = "Low или Mid или High")
    private Priority priority;
    @Schema(description = "Дата и время окончания", example = "2024-08-10T18:45:51.951731")
    private LocalDateTime completionDate;
}
