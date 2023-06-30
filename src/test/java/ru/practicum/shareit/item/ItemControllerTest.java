package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithProperty;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private Item item;
    private ItemDto itemDto;

    private User owner;
    private static String USER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder().name("name1").description("Item1").available(true).requestId(1L).build();
        owner = User.builder()
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
    }

    @Test
    void testAddingItem() throws Exception {
        when(itemService.addItem(anyLong(), any(ItemDto.class)))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));
        verify(itemService, times(1)).addItem(anyLong(), any(ItemDto.class));
    }

    @Test
    void testUdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(item);
        ItemDto itemDtoOut = ItemMapper.toItemDto(item);
        mvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemDtoOut.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoOut.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoOut.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDtoOut.getRequestId()));
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any(ItemDto.class));

    }

    @Test
    void testGettingItem() throws Exception {
        ItemWithProperty fullItem = ItemWithProperty.builder()
                .id(1)
                .name("name")
                .description("item1")
                .available(true)
                .lastBooking(new BookingShort(1, 1))
                .nextBooking(new BookingShort(2, 1))
                .comments(new ArrayList<>())
                .build();
        when(itemService.getItem(anyLong(), anyLong())).thenReturn(fullItem);

        mvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(fullItem.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(fullItem.getName()))
                .andExpect(jsonPath("$.description").value(fullItem.getDescription()))
                .andExpect(jsonPath("$.available").value(fullItem.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").isNotEmpty())
                .andExpect(jsonPath("$.nextBooking").isNotEmpty())
                .andExpect(jsonPath("$.comments").isEmpty());
        verify(itemService, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    void testGettingUserItems() throws Exception {
        ItemWithProperty fullItem = ItemWithProperty.builder()
                .id(1)
                .name("name")
                .description("item1")
                .available(true)
                .lastBooking(new BookingShort(1, 1))
                .nextBooking(new BookingShort(2, 1))
                .comments(new ArrayList<>())
                .build();
        List<ItemWithProperty> result = List.of(fullItem);
        when(itemService.getUserItems(anyLong(), anyInt(), anyInt())).thenReturn(result);
        mvc.perform(get("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(fullItem.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(fullItem.getName()))
                .andExpect(jsonPath("$[0].description").value(fullItem.getDescription()))
                .andExpect(jsonPath("$[0].available").value(fullItem.getAvailable()))
                .andExpect(jsonPath("$[0].lastBooking").isNotEmpty())
                .andExpect(jsonPath("$[0].nextBooking").isNotEmpty())
                .andExpect(jsonPath("$[0].comments").isEmpty());
        verify(itemService, times(1)).getUserItems(anyLong(), anyInt(), anyInt());

    }

    @Test
    void testSearchItem() throws Exception {
        when(itemService.searchItem(anyString(), anyInt(), anyInt())).thenReturn(List.of(item));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        List<ItemDto> result = List.of(itemDto);

        mvc.perform(get("/items/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                        .param("text", "item")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()))
        ;
        verify(itemService, times(1)).searchItem(anyString(), anyInt(), anyInt());
    }

    @Test
    void testAddComment() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CommentRequest request = CommentRequest.builder().text("good item").build();
        Comment response = Comment.builder()
                .id(1L)
                .text("good item")
                .item(item)
                .user(new User(2, "user@mail.ru", "requestor"))
                .created(now).build();

        when(itemService.addComment(anyLong(), anyLong(), any(CommentRequest.class)))
                .thenReturn(response);
        CommentResponse result = CommentMapper.toResponse(response);

        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER, "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", is(result.getId()), Long.class))
                .andExpect(jsonPath("$.text").value(result.getText()))
                .andExpect(jsonPath("$.authorName").value(result.getAuthorName()))
                .andExpect(jsonPath("$.created").isNotEmpty());
        verify(itemService, times(1)).addComment(anyLong(), anyLong(), any(CommentRequest.class));
    }
}