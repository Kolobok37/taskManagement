package com.example.taskManagement;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

public class TaskManagementApplicationTest {
    @Test
    public void test1() {
        System.out.println(11);
        Assert.assertEquals(1,1);
    }
}
