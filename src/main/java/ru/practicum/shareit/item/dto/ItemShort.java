package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemShort {

    long id;
    String name;
    long ownerId;
}
