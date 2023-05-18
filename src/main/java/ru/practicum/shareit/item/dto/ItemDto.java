package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.ItemCreate;
import ru.practicum.shareit.item.ItemId;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(toBuilder = true)
public class ItemDto {
    @Null(groups = ItemId.class)
    private long id;
    @NotNull(groups = ItemCreate.class)
    @Nullable
    private String name;
    @NotNull(groups = ItemCreate.class)
    @Nullable
    private String description;
    @NotNull(groups = ItemCreate.class)
    @Nullable
    private Boolean available;

}
