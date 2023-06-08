package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.annotation.ItemCreate;
import ru.practicum.shareit.item.annotation.ItemId;
import ru.practicum.shareit.item.comment.CommentResponse;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemWithTime {

    private long id;
    private String name;
    private String description;
   private Boolean available;

   private BookingShort lastBooking;
   private BookingShort nextBooking;

   private List<CommentResponse> comments;

}
