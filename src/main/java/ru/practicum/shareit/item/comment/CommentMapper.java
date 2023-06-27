package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(CommentRequest request, Item item, User user,LocalDateTime now) {
        return Comment.builder()
                .text(request.getText())
                .item(item)
                .user(user)
                .created(now)
                .build();
    }

    public static CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }
}
