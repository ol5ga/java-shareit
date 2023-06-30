package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import ru.practicum.shareit.items.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Generated
public class ItemRequestResponse {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
