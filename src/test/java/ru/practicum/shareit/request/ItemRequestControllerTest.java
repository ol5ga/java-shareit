package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
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

    @Test
    void addRequest() throws Exception {
        User booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        booker.setId(2);
        ItemRequestDto request = new ItemRequestDto("Request of item", 2);
        ItemRequest response = ItemRequest.builder()
                .id(1)
                .description("Request of item")
                .requestor(booker)
                .created(created)
                .build();
        when(service.addRequest(booker.getId(), request, created)).thenReturn(response);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", String.valueOf(booker.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));
    }

    @Test
    void getMyRequests() {
    }

    @Test
    void getAllRequests() {
    }

    @Test
    void getItemRequest() {
    }
}