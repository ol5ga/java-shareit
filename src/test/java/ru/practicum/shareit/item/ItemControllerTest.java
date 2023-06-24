package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    Item item;

    User owner;

    @Test
    void addItem()throws Exception {
        ItemDto itemDtoIn = ItemDto.builder().name("name1").description("Item1").available(true).requestId(1L).build();
        owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();

        when(itemService.addItem(anyLong(),any(ItemDto.class)))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                )
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
//                .andExpect(jsonPath("$.name").value(item.getName()))
//                .andExpect(jsonPath("$.description").value(item.getDescription()))
//                .andExpect(jsonPath("$.available").value(item.getAvailable()))
//                .andExpect(jsonPath("$.requestId").value(item.getRequest()));
        verify(itemService, times(1)).addItem(anyLong(), any(ItemDto.class));
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getUserItems() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void addComment() {
    }
}