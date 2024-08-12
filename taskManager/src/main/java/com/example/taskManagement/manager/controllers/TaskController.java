package com.example.taskManagement.manager.controllers;

import com.example.taskManagement.manager.dto.CommentInDto;
import com.example.taskManagement.manager.dto.CommentOutDto;
import com.example.taskManagement.manager.dto.TaskInDto;
import com.example.taskManagement.manager.dto.TaskOutDto;
import com.example.taskManagement.manager.entities.StatusTask;
import com.example.taskManagement.manager.servicesAPI.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping
@AllArgsConstructor
@Tag(name = "Менеджер задач")
public class TaskController {
    TaskService taskService;

    @PostMapping("/tasks")
    @Operation(summary = "Создание задачи")
    public ResponseEntity<TaskOutDto> createTask(@RequestBody @Valid TaskInDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Получение задачи по id")
    public ResponseEntity<TaskOutDto> getTask(@PathVariable @Valid Long taskId) {
        return taskService.getTask(taskId);
    }

    @PatchMapping("/tasks/{taskId}")
    @Operation(summary = "Обновление задачи по id")
    public ResponseEntity<TaskOutDto> updateTask(@RequestBody @Valid TaskInDto taskDto,
                                             @PathVariable Long taskId) {
        return taskService.updateTask(taskDto, taskId);
    }

    @DeleteMapping("/tasks/{taskId}")
    @Operation(summary = "Удаление задачи по id")
    public ResponseEntity<Object> deleteTask(@PathVariable Long taskId) {
        return taskService.deleteTask(taskId);
    }

    @PatchMapping("/tasks/{taskId}/status")
    @Operation(summary = "Изменение статуса")
    public ResponseEntity<TaskOutDto> changeStatusTask(@PathVariable Long taskId,@PathVariable StatusTask status) {
        return taskService.changeStatusTask(taskId, status);
    }

    @PatchMapping("/tasks/{taskId}/performer/{performerId}")
    @Operation(summary = "Смена ответственного")
    public ResponseEntity<TaskOutDto> assignPerformer(@PathVariable Long taskId,@PathVariable Long performerId) {
        return taskService.assignPerformer(taskId, performerId);
    }
    @PostMapping("/tasks/{taskId}/comment")
    @Operation(summary = "Добавление коментария")
    public ResponseEntity<CommentOutDto> addComment(@PathVariable Long taskId,
                                                    @RequestBody @Valid CommentInDto commentInDto) {
        return taskService.addComment(taskId, commentInDto);
    }
    @GetMapping("/tasks/creator/{creatorId}")
    @Operation(summary = "Поиск задач по создателю")
    public ResponseEntity<List<TaskOutDto>> getTasksCreator(@PathVariable Long creatorId,
                                                            @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) {
        return taskService.getCreatorsTasks(creatorId,from,size);
    }
    @GetMapping("/tasks/performer/{performerId}")
    @Operation(summary = "Поиск задач по исполнителю")
    public ResponseEntity<List<TaskOutDto>> getTasksPerformer(@PathVariable Long performerId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return taskService.getPerformersTasks(performerId,from,size);
    }
}
