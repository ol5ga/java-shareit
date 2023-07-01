package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private BookingService service;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private User owner;
    private User booker;
    private Item item;
    private BookingRequest request;

    @BeforeEach
    void setUp() {
        service = new BookingService(bookingRepository, userRepository, itemRepository);
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
    void testAddingBookingWithRightData() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        BookingResponse response = BookingMapper.toResponse(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.getById(item.getId())).thenReturn(item);
        BookingResponse result = service.addBooking(2, request);
        assertEquals(response.getId(), result.getId());
        assertEquals(request.getItemId(), result.getItem().getId());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(booker.getId(), result.getBooker().getId());
        verify(bookingRepository, times(1)).save(Mockito.any(Booking.class));
    }

    @Test
    void testAddingBookingWithWrongTime() {
        request.setEnd(LocalDateTime.now().minusHours(1));
        assertThrows(ValidationException.class, () -> service.addBooking(2, request));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void testAddingBookingWithWrongUser() {
        assertThrows(ChangeException.class, () -> service.addBooking(5, request));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThrows(ChangeException.class, () -> service.addBooking(item.getOwner().getId(), request));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));

    }

    @Test
    void testAddingBookingWithNotAvailableItem() {
        item.setAvailable(false);
        assertThrows(ChangeException.class, () -> service.addBooking(2, request));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void testChangeStatusWithRightData() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        BookingResponse result = service.getStatus(booking.getId(), owner.getId(), true);
        assertEquals(BookStatus.APPROVED, result.getStatus());
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
    }

    @Test
    void testChangeStatusWithWrongUser() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        assertThrows(ChangeException.class, () -> service.getStatus(booking.getId(), 5, true));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThrows(ChangeException.class, () -> service.getStatus(booking.getId(), booker.getId(), true));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void testChangeStatusWithApproved() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        booking.setStatus(BookStatus.APPROVED);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        assertThrows(ChangeException.class, () -> service.getStatus(booking.getId(), owner.getId(), true));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void testChangeStatusWithNotApproved() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        BookingResponse result = service.getStatus(booking.getId(), owner.getId(), false);
        assertEquals(BookStatus.REJECTED, result.getStatus());
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
    }

    @Test
    void testGettingBookingWithRightData() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        BookingResponse response = BookingMapper.toResponse(booking);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        BookingResponse result = service.getBooking(1, owner.getId());
        assertEquals(response, result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
        when(userRepository.existsById(booker.getId())).thenReturn(true);
        BookingResponse result2 = service.getBooking(1, booker.getId());
        assertEquals(response, result2);
        assertEquals(booking.getId(), result2.getId());
        assertEquals(booking.getItem().getId(), result2.getItem().getId());
    }

    @Test
    void testGettingBookingWithWrongUser() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        User user = new User(3, "user2@book.ru", "user2");
        assertThrows(ChangeException.class, () -> service.getBooking(booking.getId(), user.getId()));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void testGettingUsersBookingsAll() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerOrderByStartDesc(booker, PageRequest.of(1, 1))).thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "ALL", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerOrderByStartDesc(any(), any());
        verify(bookingRepository, never()).findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void testGettingUsersBookingsWithCURRENT() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "CURRENT", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingUsersBookingsWithFUTURE() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "FUTURE", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingUsersBookingsWithPAST() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "PAST", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingUsersBookingsWithWAITING() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(booker, BookStatus.WAITING, PageRequest.of(1, 1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "WAITING", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerAndStatusEqualsOrderByStartDesc(booker, BookStatus.WAITING, PageRequest.of(1, 1));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingUsersBookingsWithREJECTED() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.REJECTED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(booker, BookStatus.REJECTED, PageRequest.of(1, 1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "REJECTED", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByBookerAndStatusEqualsOrderByStartDesc(booker, BookStatus.REJECTED, PageRequest.of(1, 1));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingUsersBookingsWithWrongState() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        assertThrows(ValidationException.class, () -> service.getUserBookings(booker.getId(), " ", 1, 1));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingBookingItemALL() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerOrderByStartDesc(owner, PageRequest.of(1, 1))).thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "ALL", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByItem_OwnerOrderByStartDesc(any(), any());
        verify(bookingRepository, never()).findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void testGettingBookingItemWithCURRENT() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "CURRENT", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingBookingItemWithFUTURE() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "FUTURE", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(any(), any());
    }

    //
    @Test
    void testGettingBookingItemWithPAST() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "PAST", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingBookingItemWithWAITING() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.WAITING);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner, BookStatus.WAITING, PageRequest.of(1, 1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "WAITING", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner, BookStatus.WAITING, PageRequest.of(1, 1));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingBookingItemWithEJECTED() {
        Booking booking = BookingMapper.toBooking(request, item, booker, BookStatus.REJECTED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner, BookStatus.REJECTED, PageRequest.of(1, 1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "REJECTED", 1, 1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner, BookStatus.REJECTED, PageRequest.of(1, 1));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(any(), any());
    }

    @Test
    void testGettingBookingItemWithWrongState() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        assertThrows(ValidationException.class, () -> service.getUserItems(owner.getId(), " ", 1, 1));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(any(), any());
    }


}