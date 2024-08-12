package com.example.taskManagement;

import com.example.taskManagement.manager.dto.*;
import com.example.taskManagement.manager.entities.*;
import com.example.taskManagement.manager.exception.NotFoundException;
import com.example.taskManagement.manager.exception.ValidationDataException;
import com.example.taskManagement.manager.mappers.MapperComment;
import com.example.taskManagement.manager.mappers.MapperTask;
import com.example.taskManagement.manager.servicesImpl.TaskServiceImpl;
import com.example.taskManagement.manager.storage.CommentRepository;
import com.example.taskManagement.manager.storage.TaskRepository;
import com.example.taskManagement.manager.storage.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ServiceTaskTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private User user;
    private TaskInDto taskInDto;
    private TaskOutDto taskOutDto;
    LocalDateTime time;

    @BeforeEach
    public void setup() {
        time = LocalDateTime.now();
        taskRepository = Mockito.mock(TaskRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);

        taskService = new TaskServiceImpl(taskRepository, userRepository, commentRepository);

        task = new Task(1L, "testTask", "TestDescription", StatusTask.Working, Priority.Low, time,
                time.plusDays(1), new User(1L, "CreatorTest", "12345678",
                "test@email.com", Role.ROLE_USER), new User(2L, "PerformerTest", "12345678",
                "test1@email.com", Role.ROLE_USER), new ArrayList<>());
        taskInDto = new TaskInDto("testTask", "TestDescription", Priority.Low,
                time.plusDays(1));
        taskOutDto = new TaskOutDto(1L, "testTask", "TestDescription", StatusTask.Working.toString(), "Low", time,
                time.plusDays(1), new UserDto(1L, "CreatorTest"), new UserDto(2L, "PerformerTest"),
                new ArrayList<>());
        user = new User(1L, "CreatorTest", "12345678", "test@email.com", Role.ROLE_USER);

    }

    @Test
    void createTask_whenTaskOk_thenTaskOutDto() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        Mockito.when(taskRepository.save(any(Task.class)))
                .thenReturn(task);


        assertEquals(taskService.createTask(taskInDto), new ResponseEntity<>(taskOutDto, HttpStatus.CREATED));
    }

    @Test
    void updateTask_whenTaskIsNotExists_thenReturnNotFoundException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(taskInDto, 1L));
    }

    @Test
    void updateTask_whenTaskIsExists_thenReturnUpdateTask() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(any())).thenAnswer(invocationOnMock -> {
            return (Task) invocationOnMock.getArguments()[0];
        });

        taskInDto = new TaskInDto("UpdateTask", "UpdateTestDescription", Priority.Mid,
                time.plusDays(2));
        Task updateTask = new Task(task.getId(), taskInDto.getTitle(), taskInDto.getDescription(), task.getStatus(), taskInDto.getPriority(),
                task.getCreationDate(), taskInDto.getCompletionDate(), task.getCreator(), task.getPerformer(), task.getComments());

        assertEquals(taskService.updateTask(taskInDto, 1L),
                new ResponseEntity<>(MapperTask.mapToTaskOutDto(updateTask), HttpStatus.OK));
    }

    @Test
    void updateTask_whenUserNotCreator_thenReturnValidationDataException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user3 = new User(3L, "CreatorTest2", "12345678", "test@email.com", Role.ROLE_USER);
        task.setCreator(user3);
        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));


        taskInDto = new TaskInDto("UpdateTask", "UpdateTestDescription", Priority.Mid,
                time.plusDays(2));

        assertThrows(ValidationDataException.class, () -> taskService.updateTask(taskInDto, 1L));

    }

    @Test
    void getTask_whenTaskNotExists_thenReturnNotFoundException() {
        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTask(1L));
    }

    @Test
    void getTask_whenTaskExists_thenReturnTaskOutDto() {
        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        assertEquals(taskService.getTask(1L), new ResponseEntity<>(taskOutDto, HttpStatus.OK));
    }

    @Test
    void deleteTask_whenTaskDeleteCreator_thenReturnStatusOk() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        assertEquals(taskService.deleteTask(1L), new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    void deleteTask_whenTaskNotExists_thenReturnNotFoundException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void deleteTask_whenDeleteNotCreator_thenReturnValidationDataException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user3 = new User(3L, "CreatorTest2", "12345678", "test@email.com", Role.ROLE_USER);
        task.setCreator(user3);
        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        assertThrows(ValidationDataException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void changeStatus_whenChangeCreator_thenReturnTaskOutDtoWhitNewStatus() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(any())).thenAnswer(invocationOnMock -> {
            return (Task) invocationOnMock.getArguments()[0];
        });

        Task updateTask = new Task(task.getId(), task.getTitle(), task.getDescription(), StatusTask.Working, task.getPriority(),
                task.getCreationDate(), task.getCompletionDate(), task.getCreator(), task.getPerformer(), task.getComments());

        assertEquals(taskService.changeStatusTask(1L, StatusTask.Working),
                new ResponseEntity<>(MapperTask.mapToTaskOutDto(updateTask), HttpStatus.OK));
    }

    @Test
    void changeStatus_whenChangePerformer_thenReturnTaskOutDtoWhitNewStatus() {
        User user3 = new User(3L, "CreatorTest2", "12345678", "test@email.com", Role.ROLE_USER);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user3);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        task.setPerformer(user3);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(any())).thenAnswer(invocationOnMock -> {
            return (Task) invocationOnMock.getArguments()[0];
        });

        Task updateTask = new Task(task.getId(), task.getTitle(), task.getDescription(), StatusTask.Working, task.getPriority(),
                task.getCreationDate(), task.getCompletionDate(), task.getCreator(), user3, task.getComments());

        assertEquals(taskService.changeStatusTask(1L, StatusTask.Working),
                new ResponseEntity<>(MapperTask.mapToTaskOutDto(updateTask), HttpStatus.OK));
    }

    @Test
    void changeStatus_whenChangeNotPerformerOrCreator_thenReturnValidationDataException() {
        User user3 = new User(3L, "CreatorTest2", "12345678", "test@email.com", Role.ROLE_USER);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user3);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        assertThrows(ValidationDataException.class, () -> taskService.changeStatusTask(1L, StatusTask.Working));
    }

    @Test
    void assignPerformer_whenChangeCreator_thenReturnTaskOutDtoWhitNewPerformerAndStatus() {
        User user3 = new User(3L, "CreatorTest2", "12345678", "test@email.com", Role.ROLE_USER);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(user3));
        Mockito.when(taskRepository.save(any())).thenAnswer(invocationOnMock -> {
            return (Task) invocationOnMock.getArguments()[0];
        });

        Task updateTask = new Task(task.getId(), task.getTitle(), task.getDescription(), StatusTask.Working, task.getPriority(),
                task.getCreationDate(), task.getCompletionDate(), task.getCreator(), user3, task.getComments());

        assertEquals(taskService.assignPerformer(1L, 3L),
                new ResponseEntity<>(MapperTask.mapToTaskOutDto(updateTask), HttpStatus.OK));
    }

    @Test
    void assignPerformer_whenUserNotExists_thenReturnNotFoundException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.assignPerformer(1L, 3L));

    }

    @Test
    void assignPerformer_whenChangeNotCreator_thenReturnValidationDataException() {
        User user3 = new User(3L, "CreatorTest2", "12345678", "test@email.com", Role.ROLE_USER);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user3);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        assertThrows(ValidationDataException.class, () -> taskService.assignPerformer(1L, 3L));
    }

    @Test
    void addComment_whenTaskExists_thenReturnComment() {
        Comment comment = new Comment(null, task, "Test comment", time, user);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));

        Mockito.when(commentRepository.save(any())).thenAnswer(invocationOnMock -> {
            return (Comment) invocationOnMock.getArguments()[0];
        });
        CommentInDto commentInDto = new CommentInDto("Test comment");

        ResponseEntity<CommentOutDto> commentOutDto =taskService.addComment(1L, commentInDto);
        comment.setCreationDate(commentOutDto.getBody().getCreationDate());
        assertEquals(commentOutDto,
                new ResponseEntity<>(MapperComment.mapToCommentDto(comment), HttpStatus.CREATED));
    }
}
