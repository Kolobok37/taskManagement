package com.example.taskManagement;

import com.example.taskManagement.manager.controllers.TaskController;
import com.example.taskManagement.manager.entities.Task;
import com.example.taskManagement.manager.servicesImpl.TaskServiceImpl;
import com.example.taskManagement.manager.storage.CommentRepository;
import com.example.taskManagement.manager.storage.TaskRepository;
import com.example.taskManagement.manager.storage.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

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

    private Task employee;
    @Test
    void exampleTest(@Autowired WebTestClient webClient) {
        webClient
                .get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello World");
    }
}
