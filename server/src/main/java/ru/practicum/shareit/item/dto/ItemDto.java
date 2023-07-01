package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder(toBuilder = true)
@Generated
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
