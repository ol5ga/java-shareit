package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto request, User user){
        return ItemRequest.builder()
                .description(request.getDescription())
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

    }

    public static ItemRequestResponse toItemRequestResponse(ItemRequest itemRequest, List<ItemShort> itemList){
        return ItemRequestResponse.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemList)
                .build();
    }
}
