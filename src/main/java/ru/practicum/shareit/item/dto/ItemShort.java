package ru.practicum.shareit.item.dto;

import lombok.Builder;

@Builder
public class ItemShort {

    long id;
    String name;
    long ownerId;
}
