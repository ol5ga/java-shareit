package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentMapperTest {
    private Item item;
    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        owner = new User(1, "user@mail.ru", "User");
        booker = new User(2, "user2@ya.ru", "User2");
        item = Item.builder()
                .id(1)
                .name("name")
                .description("description item")
                .available(true)
                .owner(owner)
                .build();

    }

    @Test
    void toComment() {
        Comment result = CommentMapper.toComment(new CommentRequest("Comment about item"), item, booker, LocalDateTime.now());

        assertEquals("Comment about item", result.getText());
        assertEquals(item, result.getItem());
        assertEquals(booker, result.getUser());

    }

    @Test
    void toResponse() {
        Comment comment = Comment.builder()
                .id(1L)
                .text("Comment text")
                .item(item)
                .user(booker)
                .created(LocalDateTime.now())
                .build();

        CommentResponse result = CommentMapper.toResponse(comment);

        assertEquals(comment.getId(), result.getId());
        assertEquals("Comment text", result.getText());
        assertEquals(comment.getUser().getName(), result.getAuthorName());
    }
}