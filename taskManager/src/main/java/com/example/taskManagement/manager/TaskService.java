package com.example.taskManagement.manager;

import com.example.taskManagement.manager.model.enam.StatusTask;
import com.example.taskManagement.manager.model.dto.CommentInDto;
import com.example.taskManagement.manager.model.dto.TaskInDto;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    ResponseEntity<Object> createTask(TaskInDto taskDto);

    ResponseEntity<Object> updateTask(TaskInDto taskDto, Long taskId);

    ResponseEntity<Object> getTask(Long taskId);

    ResponseEntity<Object> deleteTask(Long taskId);

    ResponseEntity<Object> changeStatusTask(Long taskId, StatusTask newStatus);

    ResponseEntity<Object> assignPerformer(Long taskId, Long idUser);

    ResponseEntity<Object> addComment(Long idTask, CommentInDto commentInDto);

    ResponseEntity<Object> getTasksCreator(Long idCreator, Integer from, Integer size);

    ResponseEntity<Object> getTasksPerformer(Long idPerformer, Integer from, Integer size);
}
