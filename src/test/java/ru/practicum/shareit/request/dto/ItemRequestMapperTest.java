package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {
    private Item item;

    private User owner;

    private User requestor;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        owner = new User(1, "user@mail.ru", "User");
        requestor = new User(2L, "user2@ya.ru", "User2");
        item = Item.builder()
                .id(1)
                .name("name")
                .description("description item")
                .available(true)
                .owner(owner)
                .build();
    }

    @Test
    void toItemRequest() {
        ItemRequestDto request = new ItemRequestDto("text of request", requestor.getId());

        ItemRequest result = ItemRequestMapper.toItemRequest(request, requestor, now);

        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getRequestor(), result.getRequestor().getId());
        assertEquals(requestor, result.getRequestor());
    }

    @Test
    void toItemRequestResponse() {
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request")
                .requestor(requestor)
                .created(now)
                .build();
        List<ItemDto> itemList = List.of(ItemMapper.toItemDto(item));

        ItemRequestResponse result = ItemRequestMapper.toItemRequestResponse(request, itemList);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(itemList, result.getItems());
    }
}