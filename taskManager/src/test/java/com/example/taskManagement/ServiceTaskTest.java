package com.example.taskManagement;

import com.example.taskManagement.manager.controllers.TaskController;
import com.example.taskManagement.manager.dto.TaskInDto;
import com.example.taskManagement.manager.dto.TaskOutDto;
import com.example.taskManagement.manager.dto.UserDto;
import com.example.taskManagement.manager.entities.Role;
import com.example.taskManagement.manager.entities.StatusTask;
import com.example.taskManagement.manager.entities.Task;
import com.example.taskManagement.manager.entities.User;
import com.example.taskManagement.manager.exception.NotFoundException;
import com.example.taskManagement.manager.mappers.MapperTask;
import com.example.taskManagement.manager.servicesImpl.TaskServiceImpl;
import com.example.taskManagement.manager.storage.CommentRepository;
import com.example.taskManagement.manager.storage.TaskRepository;
import com.example.taskManagement.manager.storage.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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
    public void setup(){
        time = LocalDateTime.now();
        taskRepository = Mockito.mock(TaskRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);

        taskService = new TaskServiceImpl(taskRepository,userRepository,commentRepository);

        task = new Task(1L,"testTask","TestDescription", StatusTask.Working,"Low", time,
                time.plusDays(1),new User(1L, "CreatorTest","12345678",
                "test@email.com", Role.ROLE_USER),new User(2L, "PerformerTest","12345678",
                "test1@email.com", Role.ROLE_USER),new ArrayList<>());
        taskInDto = new TaskInDto("testTask","TestDescription","Low",
                time.plusDays(1));
        taskOutDto = new TaskOutDto(1L,"testTask","TestDescription", StatusTask.Working.toString(),"Low", time,
                time.plusDays(1),new UserDto(1L,"CreatorTest"),new UserDto(2L,"PerformerTest"),
                new ArrayList<>());
        user = new User(1L,"CreatorTest","12345678","test@email.com",Role.ROLE_USER);

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


        assertEquals(taskService.createTask(taskInDto), new ResponseEntity<>(taskOutDto,HttpStatus.CREATED));
    }
    @Test
    void UpdateTask_whenTaskIsNotExists_thenReturnNotFoundException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->taskService.updateTask(taskInDto,1L));
    }
    @Test
    void UpdateTask_whenTaskIsExists_thenReturnUpdateTask() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(taskRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(any())).thenAnswer(invocationOnMock -> {
            Task task = (Task) invocationOnMock.getArguments()[0];
            return task;
        });

        taskInDto = new TaskInDto("UpdateTask","UpdateTestDescription","Mid",
                time.plusDays(2));
        Task updateTask = new Task(task.getId(),taskInDto.getTitle(),taskInDto.getDescription(),task.getStatus(),taskInDto.getPriority(),
                task.getCreationDate(),taskInDto.getCompletionDate(),task.getCreator(),task.getPerformer(),task.getComments());

        assertEquals(taskService.updateTask(taskInDto,1L),
                new ResponseEntity<>(MapperTask.mapToTaskOutDto(updateTask),HttpStatus.OK));
    }
}
