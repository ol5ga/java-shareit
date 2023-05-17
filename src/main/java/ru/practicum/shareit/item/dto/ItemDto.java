package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;

    //TODO ItemRequest request
    private Long request;

//    public ItemDto(String name, String description, boolean available, Long request) {
//        this.name = name;
//        this.description = description;
//        this.available = available;
//        this.request = request;
//    }



}
