package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Builder
@Data
@Generated
public class ItemShort {

    long id;
    String name;
    long ownerId;
}
