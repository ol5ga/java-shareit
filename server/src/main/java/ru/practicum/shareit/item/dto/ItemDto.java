package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.annotation.ItemCreate;
import ru.practicum.shareit.item.annotation.ItemId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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