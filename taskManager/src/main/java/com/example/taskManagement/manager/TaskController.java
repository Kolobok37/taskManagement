package com.example.taskManagement.manager;

import com.example.taskManagement.manager.model.dto.CommentInDto;
import com.example.taskManagement.manager.model.dto.TaskInDto;
import com.example.taskManagement.manager.model.enam.StatusTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping
@AllArgsConstructor
@Tag(name = "Менеджер задач")
public class TaskController {
    TaskService taskService;

    @PostMapping("/tasks")
    @Operation(summary = "Создание задачи")
    public ResponseEntity<Object> createTask(@RequestBody @Valid TaskInDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Получение задачи по id")
    public ResponseEntity<Object> getTask(@PathVariable Long taskId) {
        return taskService.getTask(taskId);
    }

    @PatchMapping("/tasks/{taskId}")
    @Operation(summary = "Обновление задачи по id")
    public ResponseEntity<Object> updateTask(@RequestBody @Valid TaskInDto taskDto,
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
    public ResponseEntity<Object> changeStatusTask(@PathVariable Long taskId,@PathVariable StatusTask status) {
        return taskService.changeStatusTask(taskId, status);
    }

    @PatchMapping("/tasks/{taskId}/performer/{performerId}")
    @Operation(summary = "Смена ответственного")
    public ResponseEntity<Object> assignPerformer(@PathVariable Long taskId,@PathVariable Long performerId) {
        return taskService.assignPerformer(taskId, performerId);
    }
    @PostMapping("/tasks/{taskId}/comment")
    @Operation(summary = "Добавление коментария")
    public ResponseEntity<Object> addComment(@PathVariable Long taskId,@RequestBody @Valid CommentInDto commentInDto) {
        return taskService.addComment(taskId, commentInDto);
    }
    @GetMapping("/tasks/creator/{creatorId}")
    @Operation(summary = "Поиск задач по создателю")
    public ResponseEntity<Object> getTasksCreator(@PathVariable Long creatorId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return taskService.getTasksCreator(creatorId,from,size);
    }
    @GetMapping("/tasks/performer/{performerId}")
    @Operation(summary = "Поиск задач по исполнителю")
    public ResponseEntity<Object> getTasksPerformer(@PathVariable Long performerId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return taskService.getTasksPerformer(performerId,from,size);
    }
}
