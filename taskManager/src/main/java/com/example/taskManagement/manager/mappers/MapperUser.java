package com.example.taskManagement.manager.mappers;

import com.example.taskManagement.manager.entities.User;
import com.example.taskManagement.manager.dto.UserDto;

public class MapperUser {
    public static UserDto mapToUserDto(User user) {
        if(user==null){
            return null;
        }
        return new UserDto(user.getId(), user.getUsername());
    }
}
