package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest2 {

    @MockBean
    BookingService service;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private LocalDateTime now = LocalDateTime.now();

    @Test
    void addBooking() throws Exception {
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("item1")
                .available(true)
                .build();
        UserDto booker = UserDto.builder()
                .id(1L)
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        BookingRequest request = BookingRequest.builder()
                .itemId(1L)
                .start(now)
                .end(now.plusDays(2))
                .build();
        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .start(now)
                .end(now.plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookStatus.WAITING)
                .build();
        when(service.addBooking(anyLong(),any(BookingRequest.class))).thenReturn(response);

        mvc.perform(post("/bookings")
                .content(String.format("{\"itemId\": %s,\"start\": \"%s\", \"end\":\"%s\"}", item.getId(), now, LocalDateTime.now().plusDays(2)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "1")
        ).andExpect(status().isOk());

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