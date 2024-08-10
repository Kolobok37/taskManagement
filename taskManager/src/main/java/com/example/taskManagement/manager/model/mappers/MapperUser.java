package com.example.taskManagement.manager.model.mappers;

import com.example.taskManagement.manager.model.User;
import com.example.taskManagement.manager.model.dto.UserDto;

public class MapperUser {
    public static UserDto mapToUserDto(User user) {
        if(user==null){
            return null;
        }
        return new UserDto(user.getId(), user.getUsername());
    }
}
