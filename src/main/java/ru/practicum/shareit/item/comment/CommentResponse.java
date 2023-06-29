package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDateTime;

@Data
@Builder
@Generated
public class CommentResponse {

    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
