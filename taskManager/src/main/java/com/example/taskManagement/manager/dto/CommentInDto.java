package com.example.taskManagement.manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInDto {
    @Schema(description = "Текст коментария", example = "Отличная задача")
    @Size(max = 300, message = "Комментарий должен быть не более 300 символов")
    @NotBlank(message = "Комментарий задачи не может быть пустыми")
    private String textComment;
}
