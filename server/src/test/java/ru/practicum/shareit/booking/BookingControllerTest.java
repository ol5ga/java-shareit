package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    private User owner;
    private User booker;
    private Item item;
    private static String USER = "X-Sharer-User-Id";

    private BookingRequest request;
    private Booking booking;
    private BookingResponse response;
    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        booker = User.builder()
                .id(2L)
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        request = BookingRequest.builder()
                .itemId(1L)
                .start(now.plusMinutes(30))
                .end(now.plusDays(2))
                .build();
        response = BookingResponse.builder()
                .id(1L)
                .start(now.plusMinutes(30))
                .end(now.plusDays(2))
                .item(ItemMapper.toItemDto(item))
                .booker(UserMapper.toUserDto(booker))
                .status(BookStatus.WAITING)
                .build();
        booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);

    }

    @Test
    void testAddingBooking() throws Exception {
        when(service.addBooking(anyLong(), any(BookingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .content(String.format("{\"itemId\": %s,\"start\": \"%s\", \"end\":\"%s\"}", item.getId(), now.plusMinutes(30), LocalDateTime.now().plusDays(2)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, String.valueOf(booker.getId()))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(response.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(response.getStatus().name()));
        verify(service, times(1)).addBooking(anyLong(), any(BookingRequest.class));

    }

    @Test
    void testChangeStatus() throws Exception {
        when(service.getStatus(booking.getId(), owner.getId(), true))
                .thenReturn(response);

        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .header(USER, owner.getId())
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void testGettingBooking() throws Exception {
        when(service.getBooking(anyLong(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(response.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(response.getStatus().name()));
        verify(service, times(1)).getBooking(anyLong(), anyLong());
    }

    @Test
    void testGettingUsersBookings() throws Exception {
        BookingResponse response2 = BookingResponse.builder()
                .id(2L)
                .start(now.minusDays(1))
                .end(now.plusDays(1))
                .item(ItemMapper.toItemDto(item))
                .booker(UserMapper.toUserDto(booker))
                .status(BookStatus.WAITING)
                .build();

        List<BookingResponse> expectedList = List.of(response, response2);

        when(service.getUserBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(expectedList);

        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").isNotEmpty())
                .andExpect(jsonPath("$[0].end").isNotEmpty())
                .andExpect(jsonPath("$[0].item").isNotEmpty())
                .andExpect(jsonPath("$[0].booker.id").value(response.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(response.getStatus().name()))
                .andExpect(jsonPath("$[1].id", is(response2.getId()), Long.class))
                .andExpect(jsonPath("$[1].start").isNotEmpty())
                .andExpect(jsonPath("$[1].end").isNotEmpty())
                .andExpect(jsonPath("$[1].item").isNotEmpty())
                .andExpect(jsonPath("$[1].booker.id").value(response2.getBooker().getId()))
                .andExpect(jsonPath("$[1].status").value(response2.getStatus().name()));
        verify(service, times(1)).getUserBookings(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void testGettingBookingItem() throws Exception {
        Item item2 = Item.builder()
                .name("name2")
                .description("item2")
                .available(true)
                .owner(owner)
                .build();
        item.setId(2);
        BookingResponse response2 = BookingResponse.builder()
                .id(2L)
                .start(now.minusDays(1))
                .end(now.plusDays(1))
                .item(ItemMapper.toItemDto(item2))
                .booker(UserMapper.toUserDto(booker))
                .status(BookStatus.WAITING)
                .build();

        List<BookingResponse> expectedList = List.of(response, response2);
        when(service.getUserItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(expectedList);
        mockMvc.perform(get("/bookings/owner")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER, "qwerty")
                .param("state", "ALL")
                .param("from", "1")
                .param("size", "2")
        ).andExpect(status().isBadRequest());
        verify(service, never()).getUserItems(anyLong(), anyString(), anyInt(), anyInt());
    }
}