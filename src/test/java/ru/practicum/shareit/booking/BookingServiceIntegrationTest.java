package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    BookingRequest request;

    @BeforeEach
    void setUp(){
        owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        userRepository.save(owner);
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);
        booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        userRepository.save(booker);
        request = BookingRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(5))
                .end(LocalDateTime.now().plusDays(2))
                .build();

    }
    @Test
    void addBooking() {
        Booking result = service.addBooking(booker.getId(), request);
        assertNotNull(result.getId());
        assertEquals(item, result.getItem());
        assertEquals(request.getItemId(),result.getItem().getId());
        assertEquals(request.getStart(),result.getStart());
        assertEquals(request.getEnd(),result.getEnd());
    }

    @Test
    void getStatus(){
        Booking booking = service.addBooking(booker.getId(), request);
        Booking result = service.getStatus(booking.getId(), owner.getId(), true);
        assertEquals(BookStatus.APPROVED, result.getStatus());
        assertEquals(booking.getItem(),result.getItem());
        assertEquals(booking.getStart(),result.getStart());
        assertEquals(booking.getEnd(),result.getEnd());
    }

    @Test
    void getBooking(){
        Booking booking = service.addBooking(booker.getId(), request);
        booking.setStatus(BookStatus.APPROVED);
        Item ad = booking.getItem();
        Booking result = service.getBooking(booking.getId(), booker.getId());
        assertNotNull(result);
        assertEquals(booking.getId(),result.getId());
        assertEquals(request.getItemId(),result.getItem().getId());
        assertEquals(request.getStart().truncatedTo(ChronoUnit.MINUTES),result.getStart().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(request.getEnd().truncatedTo(ChronoUnit.MINUTES),result.getEnd().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void getUserBookings(){
        Booking booking = service.addBooking(booker.getId(), request);
        booking.setStatus(BookStatus.APPROVED);
        List<BookingResponse> result = service.getUserBookings(booker.getId(),"ALL", 1, 1);
        assertTrue(result.contains(booking));
        assertEquals(1,result.size());
    }

    @Test
    void getUserItems(){
        Booking booking = service.addBooking(booker.getId(), request);
        service.getStatus(booking.getId(),  owner.getId(),true);
        List<BookingResponse> result = service.getUserItems(owner.getId(), "ALL", 1, 1);
        assertNotNull(result);
        assertEquals(BookingMapper.toResponse(booking),result.get(0));
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
    }
}