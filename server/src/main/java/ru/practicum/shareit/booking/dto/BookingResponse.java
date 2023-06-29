package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Data
@Generated
public class BookingResponse {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookStatus status;
}
