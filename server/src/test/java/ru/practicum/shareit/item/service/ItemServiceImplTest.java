package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithProperty;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemService service;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    private User owner;
    private User requestor;
    private Item item;
    private ItemRequest request;

    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        service = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, requestRepository);
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
        requestor = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        requestor.setId(2);
        request = ItemRequest.builder()
                .id(1L)
                .description("itemRequest description")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void testAddingItemWithRequest() {
        ItemDto dto = ItemDto.builder()
                .name("name")
                .description("item1")
                .available(true)
                .requestId(1L)
                .build();
        item.setRequest(request);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(itemRepository.save(item)).thenReturn(item);
        Item result = service.addItem(owner.getId(), dto);

        assertEquals(item, result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getRequestId(), result.getRequest().getId());
        assertEquals(owner, result.getOwner());
    }

    @Test
    void testAddingItemWithoutRequest() {
        ItemDto dto = ItemDto.builder()
                .name("name")
                .description("item1")
                .available(true)
                .build();
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.save(item)).thenReturn(item);
        Item result = service.addItem(owner.getId(), dto);

        assertEquals(item, result);
        assertEquals(owner, result.getOwner());
    }

    @Test
    void testUpdateItemName() {
        item.setId(1);
        ItemDto dto = ItemDto.builder()
                .name("nameNew")
                .description("item1")
                .available(true)
                .build();
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Item itemDto = ItemMapper.toItem(item.getId(), owner, dto);
        when(itemRepository.save(itemDto)).thenReturn(itemDto);
        when(itemRepository.getById(itemDto.getId())).thenReturn(itemDto);
        Item result = service.updateItem(item.getId(), owner.getId(), dto);

        assertNotEquals(item, result);
        assertEquals(item.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertNotEquals(item.getName(), result.getName());
    }

    @Test
    void testUpdateItemDescription() {
        item.setId(1);
        ItemDto dto = ItemDto.builder()
                .name("name")
                .description("New item1")
                .available(true)
                .build();
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Item itemDto = ItemMapper.toItem(item.getId(), owner, dto);
        when(itemRepository.save(itemDto)).thenReturn(itemDto);
        when(itemRepository.getById(itemDto.getId())).thenReturn(itemDto);
        Item result = service.updateItem(item.getId(), owner.getId(), dto);

        assertNotEquals(item, result);
        assertEquals(item.getId(), result.getId());
        assertEquals(dto.getDescription(), result.getDescription());
        assertNotEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void testUpdateItemAvailable() {
        item.setId(1);
        ItemDto dto = ItemDto.builder()
                .name("name")
                .description("New item1")
                .available(false)
                .build();
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Item itemDto = ItemMapper.toItem(item.getId(), owner, dto);
        when(itemRepository.save(itemDto)).thenReturn(itemDto);
        when(itemRepository.getById(itemDto.getId())).thenReturn(itemDto);
        Item result = service.updateItem(item.getId(), owner.getId(), dto);

        assertNotEquals(item, result);
        assertEquals(item.getId(), result.getId());
        assertEquals(dto.getAvailable(), result.getAvailable());
        assertNotEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void testUpdateItemWrongUser() {
        item.setId(1);
        ItemDto dto = ItemDto.builder()
                .name("nameNew")
                .description("item1")
                .available(true)
                .build();
        assertThrows(EntityNotFoundException.class, () -> service.updateItem(1, 3, dto));
        assertThrows(EntityNotFoundException.class, () -> service.updateItem(1, requestor.getId(), dto));
    }

    @Test
    void testGettingItem() {
        item.setId(1);
        CommentResponse comment = CommentResponse.builder()
                .id(1)
                .text("Comment text")
                .authorName("Author")
                .created(LocalDateTime.now())
                .build();
        Booking booking = Booking.builder()
                .start(now.minusDays(2))
                .end(now.minusDays(1))
                .item(item)
                .booker(requestor)
                .status(BookStatus.APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .start(now.plusHours(2))
                .end(now.plusDays(2))
                .item(item)
                .booker(requestor)
                .status(BookStatus.APPROVED)
                .build();
        booking.setId(1);
        booking2.setId(3);
        BookingShort last = BookingMapper.toBookingShort(booking);
        BookingShort next = BookingMapper.toBookingShort(booking2);
        List<CommentResponse> comments = new ArrayList<>();
        comments.add(comment);
        ItemWithProperty fullItem = ItemWithProperty.builder()
                .id(1)
                .name("name")
                .description("item1")
                .available(true)
                .lastBooking(last)
                .nextBooking(next)
                .comments(comments)
                .build();
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(booking);
        when(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStart(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(booking2);
        ItemWithProperty result = service.getItem(fullItem.getId(), owner.getId());

        assertEquals(fullItem.getId(), result.getId());
        assertEquals(fullItem.getName(), result.getName());
        assertEquals(fullItem.getDescription(), result.getDescription());
        assertEquals(last, result.getLastBooking());
        assertEquals(next, result.getNextBooking());
    }

    @Test
    void testGetItemWithoutBooking() {
        item.setId(1);
        CommentResponse comment = CommentResponse.builder()
                .id(1)
                .text("Comment text")
                .authorName("Author")
                .created(LocalDateTime.now())
                .build();
        Booking booking = Booking.builder()
                .start(now.minusDays(2))
                .end(now.minusDays(1))
                .item(item)
                .booker(requestor)
                .status(BookStatus.APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .start(now.plusHours(2))
                .end(now.plusDays(2))
                .item(item)
                .booker(requestor)
                .status(BookStatus.APPROVED)
                .build();
        booking.setId(1);
        booking2.setId(3);
        BookingShort last = BookingMapper.toBookingShort(booking);
        BookingShort next = BookingMapper.toBookingShort(booking2);
        List<CommentResponse> comments = new ArrayList<>();
        comments.add(comment);
        ItemWithProperty fullItem = ItemWithProperty.builder()
                .id(1)
                .name("name")
                .description("item1")
                .available(true)
                .lastBooking(last)
                .nextBooking(next)
                .comments(comments)
                .build();
        when(userRepository.existsById(requestor.getId())).thenReturn(true);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(booking);
        when(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStart(any(Long.class), any(LocalDateTime.class)))
                .thenReturn(booking2);
        ItemWithProperty result = service.getItem(fullItem.getId(), requestor.getId());

        assertEquals(null, result.getLastBooking());
        assertEquals(null, result.getNextBooking());
        assertEquals(fullItem.getId(), result.getId());
        assertEquals(fullItem.getName(), result.getName());
        assertEquals(fullItem.getDescription(), result.getDescription());

        booking.setStatus(BookStatus.REJECTED);
        ItemWithProperty result2 = service.getItem(fullItem.getId(), owner.getId());
        assertEquals(null, result2.getLastBooking());
        assertEquals(next, result2.getNextBooking());
        assertEquals(fullItem.getId(), result2.getId());

        booking2.setStatus(BookStatus.REJECTED);
        ItemWithProperty result3 = service.getItem(fullItem.getId(), owner.getId());

        assertEquals(null, result3.getNextBooking());
        assertEquals(fullItem.getId(), result3.getId());
    }


//    @Test
//    void testGetUserItems() {
//        item.setId(1);
//        CommentResponse comment = CommentResponse.builder()
//                .id(1)
//                .text("Comment text")
//                .authorName("Author")
//                .created(LocalDateTime.now())
//                .build();
//        Booking booking = Booking.builder()
//                .start(now.minusDays(2))
//                .end(now.minusDays(1))
//                .item(item)
//                .booker(requestor)
//                .status(BookStatus.APPROVED)
//                .build();
//        Booking booking2 = Booking.builder()
//                .start(now.plusHours(2))
//                .end(now.plusDays(2))
//                .item(item)
//                .booker(requestor)
//                .status(BookStatus.APPROVED)
//                .build();
//        booking.setId(1);
//        booking2.setId(3);
//        List<CommentResponse> comments = new ArrayList<>();
//        comments.add(comment);
//        List<Item> items = new ArrayList<>();
//        items.add(item);
//        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
//        when(itemRepository.findByOwnerId(owner.getId(), PageRequest.of(1, 1))).thenReturn(items);
//        when(bookingRepository.findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(booking);
//        when(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStart(any(Long.class), any(LocalDateTime.class)))
//                .thenReturn(booking2);
//        List<ItemWithProperty> result = service.getUserItems(owner.getId(), 1, 1);
//
//        assertEquals(1, result.size());
//        assertEquals(item.getId(), result.get(0).getId());
//    }

//    @Test
//    void testSearchItem() {
//        List<Item> items = new ArrayList<>();
//        items.add(item);
//        when(itemRepository.search("item", PageRequest.of(1, 1))).thenReturn(items);
//        List<Item> result = service.searchItem("item", 1, 1);
//
//        assertEquals(1, result.size());
//        assertEquals(item, result.get(0));
//
//    }

    @Test
    void testAddingComment() {
        item.setId(1);
        Booking booking = Booking.builder()
                .start(now.minusDays(2).truncatedTo(ChronoUnit.MINUTES))
                .end(now.minusDays(1).truncatedTo(ChronoUnit.MINUTES))
                .item(item)
                .booker(requestor)
                .status(BookStatus.APPROVED)
                .build();
        booking.setId(1L);
        CommentRequest commentRequest = new CommentRequest("Comment text");
        Comment comment = CommentMapper.toComment(commentRequest, item, requestor, LocalDateTime.now());
        when(itemRepository.getById(item.getId())).thenReturn(item);
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(booking);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocationOnMock -> {
            comment.setId(1L);
            return comment;
        });

        Comment result = service.addComment(requestor.getId(), item.getId(), commentRequest);

        assertEquals(commentRequest.getText(), result.getText());
        assertEquals(item, result.getItem());
        assertEquals(requestor, result.getUser());

    }
}