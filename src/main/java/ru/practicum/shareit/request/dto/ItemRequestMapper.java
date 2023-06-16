package ru.practicum.shareit.request.dto;

import net.bytebuddy.asm.Advice;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto request, User user, LocalDateTime created){
        return ItemRequest.builder()
                .description(request.getDescription())
                .requestor(user)
                .created(created)
                .build();

    }

    public static ItemRequestResponse toItemRequestResponse(ItemRequest itemRequest, List<ItemDto> itemList){
        return ItemRequestResponse.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemList)
                .build();
    }
}
