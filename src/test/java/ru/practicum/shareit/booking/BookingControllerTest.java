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
import java.util.List;

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


    private BookingRequest request;
    private Booking booking;
    private BookingResponse response;

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
        booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        response = BookingMapper.toResponse(booking);
        response.setId(1);

    }
    @Test
    void addBooking() throws Exception {
//        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
//        BookingResponse response = BookingMapper.toResponse(booking);
//        response.setId(1);
//        response.setStatus(BookStatus.WAITING);
//        when(service.addBooking(anyLong(),any(BookingRequest.class))).thenReturn(response);
////                thenAnswer(invocationOnMock -> {
////                    Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
////                    booking.setId(1);
////                    return booking;
////                });
//
//        mockMvc.perform(post("/bookings")
//                                .content(mapper.writeValueAsString(request))
//                .characterEncoding(StandardCharsets.UTF_8)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .header("X-Sharer-User-Id", "1")
//                ).andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
//                .andExpect(jsonPath("$.start").isNotEmpty())
//                .andExpect(jsonPath("$.end").isNotEmpty())
//                .andExpect(jsonPath("$.item").isNotEmpty())
//                .andExpect(jsonPath("$.booker.id").value(response.getBooker().getId()))
//                .andExpect(jsonPath("$.status").value(response.getStatus().name()));
//        verify(service, times(1)).addBooking(anyLong(), any(BookingRequest.class));
////
////                        .header("X-Sharer-User-Id", String.valueOf(booker.getId()))
////                        .characterEncoding(StandardCharsets.UTF_8)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(mapper.writeValueAsString(request))
////                        .accept(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id").value(1L))
////                .andExpect(jsonPath("$.item.name").value("name"));
    }

    @Test
    void getStatus() throws Exception {
//        response.setStatus(BookStatus.WAITING);
        when(service.getStatus(booking.getId(),owner.getId(), true))
                .thenReturn(response);

        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void getBooking() throws Exception {
        when(service.getBooking(response.getBooker().getId(), response.getId()))
                .thenReturn(response);

        mockMvc.perform(get("/bookings/{bookingId}", response.getId())
                        .header("X-Sharer-User-Id", booker.getId()))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(response.getId()))
//                .andExpect(jsonPath("$.status").value(response.getStatus()));
    }

    @Test
    void getUserBookings() throws Exception {
        List<BookingResponse> expectedList;

        Object UserMapper;
        BookingResponse newBooking = BookingResponse.builder()
                .id(2L)
                .start(request.getStart())
                .end(request.getEnd())
                .status(BookStatus.APPROVED)
//                .booker(UserMapper.toUserDto(booker))
//                .item(ItemMapper.itemDtoOf(item))
                .build();

        expectedList = List.of(response, newBooking);

                when(service.getUserBookings(booker.getId(), "ALL",  0, 10))
                .thenReturn(expectedList);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(expectedList.get(1).getId()));
    }

    @Test
    void getUserItems() {
    }
}