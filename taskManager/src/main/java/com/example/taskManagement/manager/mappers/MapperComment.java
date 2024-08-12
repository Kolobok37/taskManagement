package com.example.taskManagement.manager.mappers;

import com.example.taskManagement.manager.entities.Comment;
import com.example.taskManagement.manager.dto.CommentOutDto;

public class MapperComment {
    public static CommentOutDto mapToCommentDto(Comment comment) {
        return new CommentOutDto(comment.getId(), comment.getTask().getId(), comment.getText(), comment.getCreationDate(),
                comment.getCreator().getId(),comment.getTask().getId());
    }
}
