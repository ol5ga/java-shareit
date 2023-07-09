package ru.practicum.shareit.items.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.items.annotation.ItemCreate;
import ru.practicum.shareit.items.annotation.ItemId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
@Generated
public class ItemDto {
    @Null(groups = ItemId.class)
    private long id;
    @NotBlank(groups = ItemCreate.class)
    @Nullable
    private String name;
    @NotNull(groups = ItemCreate.class)
    @Nullable
    private String description;
    @NotNull(groups = ItemCreate.class)
    @Nullable
    private Boolean available;
    @Nullable
    private Long requestId;
}
