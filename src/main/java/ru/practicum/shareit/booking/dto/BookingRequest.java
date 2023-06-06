package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingRequest {

    @NotNull
    Long itemId;

    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    @Future
    LocalDateTime end;
}
