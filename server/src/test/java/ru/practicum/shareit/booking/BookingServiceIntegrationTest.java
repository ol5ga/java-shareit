package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService service;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;
    private User owner;
    private User booker;
    private Item item;
    private BookingRequest request;

//    @BeforeEach
//    void setUp() {
//        owner = User.builder()
//                .email("ownerItem1@Mail.ru")
//                .name("ownerItem1")
//                .build();
//        userRepository.save(owner);
//        item = Item.builder()
//                .name("name")
//                .description("item1")
//                .available(true)
//                .owner(owner)
//                .build();
//        itemRepository.save(item);
//        booker = User.builder()
//                .email("bookerItem1@mail.ru")
//                .name("booker")
//                .build();
//        userRepository.save(booker);
//        request = BookingRequest.builder()
//                .itemId(1L)
//                .start(LocalDateTime.now().plusMinutes(5))
//                .end(LocalDateTime.now().plusDays(2))
//                .build();
//
//    }
//
//    @Test
//    void testAddingBooking() {
//        BookingResponse result = service.addBooking(booker.getId(), request);
//        assertNotNull(result.getId());
//        assertEquals(item.getId(), result.getItem().getId());
//        assertEquals(request.getItemId(), result.getItem().getId());
//        assertEquals(request.getStart(), result.getStart());
//        assertEquals(request.getEnd(), result.getEnd());
//    }
//
//    @Test
//    void testChangeStatus() {
//        BookingResponse booking = service.addBooking(booker.getId(), request);
//        BookingResponse result = service.getStatus(booking.getId(), owner.getId(), true);
//        assertEquals(BookStatus.APPROVED, result.getStatus());
//        assertEquals(booking.getItem(), result.getItem());
//        assertEquals(booking.getStart().truncatedTo(ChronoUnit.MINUTES), result.getStart().truncatedTo(ChronoUnit.MINUTES));
//        assertEquals(booking.getEnd().truncatedTo(ChronoUnit.MINUTES), result.getEnd().truncatedTo(ChronoUnit.MINUTES));
//    }
//
//    @Test
//    void testGettingBooking() {
//        BookingResponse booking = service.addBooking(booker.getId(), request);
//        booking.setStatus(BookStatus.APPROVED);
//        BookingResponse result = service.getBooking(booking.getId(), booker.getId());
//        assertNotNull(result);
//        assertEquals(booking.getId(), result.getId());
//        assertEquals(request.getItemId(), result.getItem().getId());
//        assertEquals(request.getStart().truncatedTo(ChronoUnit.MINUTES), result.getStart().truncatedTo(ChronoUnit.MINUTES));
//        assertEquals(request.getEnd().truncatedTo(ChronoUnit.MINUTES), result.getEnd().truncatedTo(ChronoUnit.MINUTES));
//    }
//
//    @Test
//    void testGettingUsersBookings() {
//        BookingResponse booking = service.addBooking(booker.getId(), request);
//        booking.setStatus(BookStatus.APPROVED);
//        booking = service.getStatus(booking.getId(), owner.getId(), true);
//        List<BookingResponse> result = service.getUserBookings(booker.getId(), "ALL", 0, 1);
//        assertEquals(1, result.size());
//        assertEquals(result.get(0).getId(), booking.getId());
//        assertEquals(result.get(0).getItem().getId(), booking.getItem().getId());
//    }
//
//    @Test
//    void testGettingBookingItem() {
//        BookingResponse booking = service.addBooking(booker.getId(), request);
//        List<BookingResponse> result = service.getUserItems(owner.getId(), "ALL", 0, 1);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(booking.getId(), result.get(0).getId());
//    }
}