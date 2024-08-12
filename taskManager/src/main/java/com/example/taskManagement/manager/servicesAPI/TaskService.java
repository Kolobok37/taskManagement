package com.example.taskManagement.manager.servicesAPI;

import com.example.taskManagement.manager.dto.CommentOutDto;
import com.example.taskManagement.manager.dto.TaskOutDto;
import com.example.taskManagement.manager.entities.StatusTask;
import com.example.taskManagement.manager.dto.CommentInDto;
import com.example.taskManagement.manager.dto.TaskInDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    ResponseEntity<TaskOutDto> createTask(TaskInDto taskDto);

    ResponseEntity<TaskOutDto> updateTask(TaskInDto taskDto, Long taskId);

    ResponseEntity<TaskOutDto> getTask(Long taskId);

    ResponseEntity<Object> deleteTask(Long taskId);

    ResponseEntity<TaskOutDto> changeStatusTask(Long taskId, StatusTask newStatus);

    ResponseEntity<TaskOutDto> assignPerformer(Long taskId, Long idUser);

    ResponseEntity<CommentOutDto> addComment(Long idTask, CommentInDto commentInDto);

    ResponseEntity<List<TaskOutDto>> getCreatorsTasks(Long idCreator, Integer from, Integer size);

    ResponseEntity<List<TaskOutDto>> getPerformersTasks(Long idPerformer, Integer from, Integer size);
}
