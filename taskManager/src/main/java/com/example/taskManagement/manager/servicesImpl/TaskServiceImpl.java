package com.example.taskManagement.manager.servicesImpl;


import com.example.taskManagement.manager.dto.CommentOutDto;
import com.example.taskManagement.manager.dto.TaskOutDto;
import com.example.taskManagement.manager.paging.Paging;
import com.example.taskManagement.manager.servicesAPI.TaskService;
import com.example.taskManagement.manager.exception.NotFoundException;
import com.example.taskManagement.manager.exception.ValidationDataException;
import com.example.taskManagement.manager.entities.Comment;
import com.example.taskManagement.manager.entities.StatusTask;
import com.example.taskManagement.manager.entities.Task;
import com.example.taskManagement.manager.entities.User;
import com.example.taskManagement.manager.dto.CommentInDto;
import com.example.taskManagement.manager.dto.TaskInDto;
import com.example.taskManagement.manager.mappers.MapperComment;
import com.example.taskManagement.manager.mappers.MapperTask;
import com.example.taskManagement.manager.storage.CommentRepository;
import com.example.taskManagement.manager.storage.TaskRepository;
import com.example.taskManagement.manager.storage.UserRepository;
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
    public ResponseEntity<TaskOutDto> createTask(TaskInDto taskInDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = MapperTask.mapToTask(taskInDto);
        task.setCreationDate(LocalDateTime.now());
        task.setCreator(user);
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(taskRepository.save(task)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TaskOutDto> updateTask(TaskInDto taskInDto, Long taskId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        if (!user.getId().equals(task.getCreator().getId())) {
            throw new ValidationDataException("Error: You can only edit your created tasks");
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
        if(taskInDto.getPriority()!=null){
            task.setPriority(taskInDto.getPriority());
        }
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(taskRepository.save(task)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TaskOutDto> getTask(Long taskId) {
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
    public ResponseEntity<TaskOutDto> changeStatusTask(Long taskId, StatusTask newStatus) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        if (!user.getId().equals(task.getCreator().getId())
                && !user.getId().equals(task.getPerformer().getId())) {
            throw new ValidationDataException("You can only change status your created tasks or if you are a performer");
        }
        task.setStatus(newStatus);
        taskRepository.save(task);
        return new ResponseEntity<>(MapperTask.mapToTaskOutDto(task), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TaskOutDto> assignPerformer(Long taskId, Long idUser) {
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
    public ResponseEntity<CommentOutDto> addComment(Long taskId, CommentInDto commentInDto) {
        User userCreator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d is not found.", taskId)));
        Comment comment = new Comment(null, task, commentInDto.getTextComment(), LocalDateTime.now(), userCreator);
        return new ResponseEntity<>(MapperComment.mapToCommentDto(commentRepository.save(comment)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<TaskOutDto>> getCreatorsTasks(Long idCreator, Integer from, Integer size) {
        List<Task> tasks = taskRepository.findByCreatorId(idCreator, Paging.paging(from, size));
        return new ResponseEntity<>(tasks.stream().map(MapperTask::mapToTaskOutDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TaskOutDto>> getPerformersTasks(Long idPerformer, Integer from, Integer size) {
        List<Task> tasks = taskRepository.findByPerformerId(idPerformer,Paging.paging(from, size));
        return new ResponseEntity<>(tasks.stream().map(MapperTask::mapToTaskOutDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
