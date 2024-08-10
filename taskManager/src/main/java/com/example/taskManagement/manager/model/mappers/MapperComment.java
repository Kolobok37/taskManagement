package com.example.taskManagement.manager.model.mappers;

import com.example.taskManagement.manager.model.Comment;
import com.example.taskManagement.manager.model.dto.CommentOutDto;

public class MapperComment {
    public static CommentOutDto mapToCommentDto(Comment comment) {
        return new CommentOutDto(comment.getId(), comment.getTask().getId(), comment.getText(), comment.getCreationDate(),
                comment.getCreator().getId(),comment.getTask().getId());
    }
}
