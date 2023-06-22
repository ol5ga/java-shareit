package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    private Item item;
    private User owner;
    private User booker;
    @BeforeEach
    void setUp() {
        owner = new User(1, "user@mail.ru", "User");
        booker = new User(2,"user2@ya.ru","User2");
        item = Item.builder()
                .id(1)
                .name("name")
                .description("description item")
                .available(true)
                .owner(owner)
                .request(new ItemRequest(1,"ItemRequest",booker, LocalDateTime.now()))
                .build();
    }

    @Test
    void toItemDto() {
        ItemDto result = ItemMapper.toItemDto(item);

        assertEquals(item.getId(),result.getId());
        assertEquals(item.getName(),result.getName());
        assertEquals(item.getRequest().getId(),result.getRequestId());
    }

    @Test
    void toItem() {
        ItemDto dto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("itemDescription")
                .available(true)
                .build();
        Item result = ItemMapper.toItem(dto.getId(),owner,dto);

        assertEquals(dto.getId(),result.getId());
        assertEquals(dto.getName(),result.getName());
        assertEquals(dto.getDescription(),result.getDescription());

    }

    @Test
    void testToItem() {
        ItemDto dto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("itemDescription")
                .available(true)
                .requestId(1L)
                .build();
        ItemRequest request = new ItemRequest(1,"ItemRequest",booker, LocalDateTime.now());
        Item result = ItemMapper.toItem(dto.getId(),owner,dto,request);

        assertEquals(dto.getId(),result.getId());
        assertEquals(dto.getName(),result.getName());
        assertEquals(dto.getDescription(),result.getDescription());
        assertEquals(dto.getRequestId(),result.getRequest().getId());
        assertEquals(owner,result.getOwner());
        assertEquals(request,result.getRequest());
    }
    @Test
    void toItemWithTime() {
        BookingShort last = BookingShort.builder()
                .id(1)
                .bookerId(2)
                .build();
        BookingShort next = BookingShort.builder()
                .id(3)
                .bookerId(3)
                .build();
        CommentResponse comment = CommentResponse.builder()
                .id(1)
                .text("Comment text")
                .authorName("Author")
                .created(LocalDateTime.now())
                .build();

        List<CommentResponse> comments = new ArrayList<>();
        comments.add(comment);
        ItemWithProperty result = ItemMapper.toItemWithTime(item,last,next,comments);

        assertEquals(item.getId(),result.getId());
        assertEquals(item.getName(),result.getName());
        assertEquals(item.getDescription(),result.getDescription());
        assertEquals(last,result.getLastBooking());
        assertEquals(next, result.getNextBooking());
        assertEquals(comments,result.getComments());
    }
}