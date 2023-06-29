package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comment.CommentResponse;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Generated
public class ItemWithProperty {

    private long id;
    private String name;
    private String description;
    private Boolean available;

    private BookingShort lastBooking;
    private BookingShort nextBooking;

    private List<CommentResponse> comments;


}
