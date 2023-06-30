package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookingMapperTest {

    private Item item;

    private User owner;
    private User booker;

    @BeforeEach
    void setUp() {
        owner = new User(1, "user@mail.ru", "User");
        booker = new User(2, "user2@ya.ru", "User2");
        item = Item.builder()
                .id(1)
                .name("name")
                .description("description item")
                .available(true)
                .owner(owner)
                .build();
    }

    @Test
    void testMappingToBooking() {
        BookingRequest request = BookingRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .build();
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);

        assertEquals(request.getStart(), booking.getStart());
        assertEquals(request.getEnd(), booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookStatus.WAITING, booking.getStatus());
    }

    @Test
    void testMappingToResponse() {

        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2), item, booker, BookStatus.WAITING);
        BookingResponse result = BookingMapper.toResponse(booking);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
        assertNotEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker().getId(), result.getBooker().getId());
        assertNotEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void testMappingToBookingShort() {
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2), item, booker, BookStatus.WAITING);
        BookingShort result = BookingMapper.toBookingShort(booking);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker().getId(), result.getBookerId());
    }
}