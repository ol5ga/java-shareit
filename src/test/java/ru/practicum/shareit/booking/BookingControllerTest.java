package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    BookingService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
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
        owner.setId(1);
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        item.setId(1);
        booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        booker.setId(2);
        request = BookingRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .build();

    }
    @Test
    void addBooking() throws Exception {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
//        booking.setId(1);
        when(service.addBooking(booker.getId(), request)).
                thenAnswer(invocationOnMock -> {
                    booking.setId(1);
                    return BookingMapper.toResponse(booking);
                });
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", String.valueOf(booker.getId()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("drill"));
    }

    @Test
    void getStatus() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getUserBookings() {
    }

    @Test
    void getUserItems() {
    }
}