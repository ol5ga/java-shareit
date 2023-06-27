package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
