package com.example.taskManagement.manager;


import com.example.taskManagement.manager.exception.NotFoundException;
import com.example.taskManagement.manager.exception.ValidationDataException;
import com.example.taskManagement.manager.model.Comment;
import com.example.taskManagement.manager.model.enam.StatusTask;
import com.example.taskManagement.manager.model.Task;
import com.example.taskManagement.manager.model.User;
import com.example.taskManagement.manager.model.dto.CommentInDto;
import com.example.taskManagement.manager.model.dto.TaskInDto;
import com.example.taskManagement.manager.model.mappers.MapperComment;
import com.example.taskManagement.manager.model.mappers.MapperTask;
import com.example.taskManagement.manager.storage.CommentRepository;
import com.example.taskManagement.manager.storage.TaskRepository;
import com.example.taskManagement.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ResponseEntity<Object> createTask(TaskInDto taskInDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = MapperTask.mapToTask(taskInDto);
        task.setCreationDate(LocalDateTime.now());
        task.setCreator(user);
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(taskRepository.save(task)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateTask(TaskInDto taskInDto, Long taskId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        if (!user.getId().equals(task.getCreator().getId())) {
            throw new ValidationDataException("You can only edit your created tasks");
        }
        if(taskInDto.getTitle()!=null){
            task.setTitle(taskInDto.getTitle());
        }
        if(taskInDto.getDescription()!=null){
            task.setDescription(taskInDto.getDescription());
        }
        if(taskInDto.getCompletionDate()!=null){
            task.setCompletionDate(taskInDto.getCompletionDate());
        }
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(taskRepository.save(task)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTask(Long taskId) {
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)))),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteTask(Long taskId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        if (!user.getId().equals(task.getCreator().getId())) {
            throw new ValidationDataException("You can only delete your created tasks");
        }
        taskRepository.deleteById(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> changeStatusTask(Long taskId, StatusTask newStatus) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        if (!user.getId().equals(task.getCreator().getId())
                || !user.getId().equals(task.getPerformer().getId())) {
            throw new ValidationDataException("You can only change status your created tasks or if you are a performer");
        }
        task.setStatus(newStatus);
        taskRepository.save(task);
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(task), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> assignPerformer(Long taskId, Long idUser) {
        User userCreator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        if (!userCreator.getId().equals(task.getCreator().getId())) {
            throw new ValidationDataException("You can only assign performer your created tasks");
        }
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException(String.format("User %d is not found.", idUser)));
        task.setPerformer(user);
        task.setStatus(StatusTask.Working);
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(taskRepository.save(task)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addComment(Long taskId, CommentInDto commentInDto) {
        User userCreator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        Comment comment = new Comment(null, task, commentInDto.getText(), LocalDateTime.now(), userCreator);
        return new ResponseEntity<>(MapperComment.mapToCommentDto(commentRepository.save(comment)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTasksCreator(Long idCreator, Integer from, Integer size) {
        List<Task> tasks = taskRepository.findByCreatorId(idCreator, Paging.paging(from, size));
        return new ResponseEntity<>(tasks.stream().map(MapperTask::mapToTaskOutDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTasksPerformer(Long idPerformer, Integer from, Integer size) {
        List<Task> tasks = taskRepository.findByPerformerId(idPerformer,Paging.paging(from, size));
        return new ResponseEntity<>(tasks.stream().map(MapperTask::mapToTaskOutDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
