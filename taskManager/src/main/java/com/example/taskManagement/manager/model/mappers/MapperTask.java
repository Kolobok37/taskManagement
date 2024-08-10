package com.example.taskManagement.manager.model.mappers;

import com.example.taskManagement.manager.model.Comment;
import com.example.taskManagement.manager.model.enam.StatusTask;
import com.example.taskManagement.manager.model.Task;
import com.example.taskManagement.manager.model.dto.TaskOutDto;
import com.example.taskManagement.manager.model.dto.TaskInDto;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MapperTask {
    public static Task mapToTask(TaskInDto taskDto) {
        return new Task(null, taskDto.getTitle(), taskDto.getDescription(), StatusTask.Created,taskDto.getPriority(),
                null, taskDto.getCompletionDate(), null, null,
                new ArrayList<Comment>());
    }

    public static TaskOutDto mapToTaskOutDto(Task task) {
        return new TaskOutDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus().toString(),
                task.getCreationDate(), task.getCompletionDate(), MapperUser.mapToUserDto(task.getCreator()),
                MapperUser.mapToUserDto(task.getPerformer()),
                task.getComments().stream().map(MapperComment::mapToCommentDto).collect(Collectors.toList()));
    }
}
