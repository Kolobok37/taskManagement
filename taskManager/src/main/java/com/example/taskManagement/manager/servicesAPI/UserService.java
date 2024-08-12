package com.example.taskManagement.manager.servicesAPI;

import com.example.taskManagement.manager.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User save(User user);

    User create(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    void getAdmin();
}
