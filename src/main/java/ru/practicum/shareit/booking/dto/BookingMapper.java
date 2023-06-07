package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking toBooking(BookingRequest request, Item item, User user, BookStatus status){
        return Booking.builder()
                .start(request.getStart())
                .end(request.getEnd())
                .item(item)
                .booker(user)
                .status(status)
                .build();
    }

    public static BookingResponse toResponse(Booking booking){
        ItemDto item = ItemDto.builder()
                .id(booking.getItem().getId())
                .name(booking.getItem().getName())
                .build();

        UserDto booker = UserDto.builder()
                .id(booking.getBooker().getId())
                .build();

        return BookingResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(booker)
                .status(booking.getStatus())
                .build();
    }

    public static BookingShort toBookingShort(Booking booking){
        return BookingShort.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

}
