package com.example.taskManagement.manager.mappers;


import com.example.taskManagement.manager.dto.TaskInDto;
import com.example.taskManagement.manager.dto.TaskOutDto;
import com.example.taskManagement.manager.entities.Comment;
import com.example.taskManagement.manager.entities.StatusTask;
import com.example.taskManagement.manager.entities.Task;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MapperTask {
    public static Task mapToTask(TaskInDto taskInDto) {
        return new Task(null, taskInDto.getTitle(), taskInDto.getDescription(), StatusTask.Created,
                taskInDto.getPriority(), null, taskInDto.getCompletionDate(), null, null,
                new ArrayList<Comment>());
    }

    public static TaskOutDto mapToTaskOutDto(Task task) {
        return new TaskOutDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus().toString(),
                task.getPriority().toString(), task.getCreationDate(), task.getCompletionDate(),
                MapperUser.mapToUserDto(task.getCreator()), MapperUser.mapToUserDto(task.getPerformer()),
                task.getComments().stream().map(MapperComment::mapToCommentDto).collect(Collectors.toList()));
    }
}
