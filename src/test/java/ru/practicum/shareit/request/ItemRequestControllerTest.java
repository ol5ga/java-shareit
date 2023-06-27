package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService service;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    LocalDateTime created = LocalDateTime.now();

    private User booker;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private ItemRequestResponse itemRequestResponse;

    @BeforeEach
    void setUp() {
        booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        booker.setId(2L);
        itemRequestDto = new ItemRequestDto("Request of item", 2);
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Request of item")
                .requestor(booker)
                .created(created)
                .build();
        itemRequestResponse = ItemRequestMapper.toItemRequestResponse(itemRequest, new ArrayList<>());
    }

    @Test
    void addRequest() throws Exception {
        when(service.addRequest(anyLong(), any(ItemRequestDto.class), any(LocalDateTime.class))).thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", String.valueOf(booker.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequest.getId()))
                .andExpect(jsonPath("$.description").value(itemRequest.getDescription()));
    }

    @Test
    void getMyRequests() throws Exception {
        ItemRequestResponse response2 = ItemRequestResponse.builder()
                .id(2)
                .description("response item 2")
                .created(created.plusHours(2))
                .items(new ArrayList<>())
                .build();
        when(service.getMyRequests(anyLong())).thenReturn(List.of(itemRequestResponse, response2));
        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$[0].created").isNotEmpty())
                .andExpect(jsonPath("$[0].items").isEmpty())
                .andExpect(jsonPath("$[1].id", is(response2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description").value(response2.getDescription()))
                .andExpect(jsonPath("$[1].created").isNotEmpty())
                .andExpect(jsonPath("$[1].items").isEmpty());
        verify(service, times(1)).getMyRequests(anyLong());
    }

    @Test
    void getAllRequests() throws Exception {
        ItemRequestResponse response2 = ItemRequestResponse.builder()
                .id(2)
                .description("response item 2")
                .created(created.plusHours(2))
                .items(new ArrayList<>())
                .build();
        when(service.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestResponse, response2));
        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$[0].created").isNotEmpty())
                .andExpect(jsonPath("$[0].items").isEmpty())
                .andExpect(jsonPath("$[1].id", is(response2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description").value(response2.getDescription()))
                .andExpect(jsonPath("$[1].created").isNotEmpty())
                .andExpect(jsonPath("$[1].items").isEmpty());
        verify(service, times(1)).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getItemRequest() throws Exception {
        when(service.getRequest(anyLong(), anyLong())).thenReturn(itemRequestResponse);
        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", is(itemRequestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.items").isEmpty());
        verify(service, times(1)).getRequest(anyLong(), anyLong());
    }
}