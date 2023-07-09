package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDateTime;

@Data
@Builder
@Generated
public class BookingRequest {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
