package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithProperty;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceIntegrationTest {
    @Autowired
    private ItemService service;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

//    private User owner;
//    private User booker;
//    private Item item;
//    private BookingRequest request;
//
//    private ItemDto itemDto;
//    private LocalDateTime now = LocalDateTime.now();
//
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
//        itemDto = ItemDto.builder()
//                .name("name")
//                .description("item1")
//                .available(true)
//                .build();
//
//    }
//
//    @Test
//    void testAddingItem() {
//        Item result = service.addItem(owner.getId(), itemDto);
//
//        assertNotNull(result.getId());
//        assertEquals(itemDto.getName(), result.getName());
//        assertEquals(itemDto.getAvailable(), result.getAvailable());
//    }
//
//    @Test
//    void testUdateItem() {
//        itemDto.setName("nameNew");
//        Item result = service.updateItem(item.getId(), owner.getId(), itemDto);
//
//        assertEquals("nameNew", result.getName());
//
//        itemDto.setDescription("New description");
//        Item result2 = service.updateItem(item.getId(), owner.getId(), itemDto);
//
//        assertEquals("New description", result2.getDescription());
//
//        itemDto.setAvailable(false);
//        Item result3 = service.updateItem(item.getId(), owner.getId(), itemDto);
//
//        assertEquals(false, result3.getAvailable());
//
//    }
//
//    @Test
//    void testGettingItem() {
//        Comment comment = Comment.builder()
//                .text("Comment text")
//                .created(LocalDateTime.now())
//                .build();
//        commentRepository.save(comment);
//        Booking booking = Booking.builder()
//                .start(now.minusDays(2))
//                .end(now.minusDays(1))
//                .item(item)
//                .booker(booker)
//                .status(BookStatus.APPROVED)
//                .build();
//        Booking booking2 = Booking.builder()
//                .start(now.plusHours(2))
//                .end(now.plusDays(2))
//                .item(item)
//                .booker(booker)
//                .status(BookStatus.APPROVED)
//                .build();
//        bookingRepository.save(booking);
//        bookingRepository.save(booking2);
//
//        ItemWithProperty result = service.getItem(item.getId(), owner.getId());
//
//        assertNotNull(result);
//        assertEquals(itemDto.getName(), result.getName());
//        assertEquals(booking.getId(), result.getLastBooking().getId());
//        assertEquals(booking2.getId(), result.getNextBooking().getId());
//    }
//
//    @Test
//    void testGettingUserItems() {
//        Comment comment = Comment.builder()
//                .text("Comment text")
//                .created(LocalDateTime.now())
//                .build();
//        commentRepository.save(comment);
//        Booking booking = Booking.builder()
//                .start(now.minusDays(2))
//                .end(now.minusDays(1))
//                .item(item)
//                .booker(booker)
//                .status(BookStatus.APPROVED)
//                .build();
//        Booking booking2 = Booking.builder()
//                .start(now.plusHours(2))
//                .end(now.plusDays(2))
//                .item(item)
//                .booker(booker)
//                .status(BookStatus.APPROVED)
//                .build();
//        bookingRepository.save(booking);
//        bookingRepository.save(booking2);
//        List<ItemWithProperty> result = service.getUserItems(owner.getId(), 0, 1);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(item.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void testSearchItem() {
//        List<Item> result = service.searchItem("item", 0, 1);
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(item.getId(), result.get(0).getId());
//    }
//
//    @Test
//    void testAddComment() {
//        CommentRequest commentRequest = new CommentRequest("Comment for item1");
//        Booking booking = Booking.builder()
//                .start(now.minusDays(2))
//                .end(now.minusDays(1))
//                .item(item)
//                .booker(booker)
//                .status(BookStatus.APPROVED)
//                .build();
//        bookingRepository.save(booking);
//        Comment result = service.addComment(booker.getId(), item.getId(), commentRequest);
//
//        assertEquals(1, result.getId());
//        assertEquals(commentRequest.getText(), result.getText());
//        assertEquals(booker.getId(), result.getUser().getId());
//        assertEquals(item.getId(), result.getItem().getId());
//    }
}