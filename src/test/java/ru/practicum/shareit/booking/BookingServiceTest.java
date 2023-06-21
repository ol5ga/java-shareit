package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    BookingRequest request;

    @BeforeEach
    void setUp(){
        service = new BookingService(bookingRepository,userRepository,itemRepository);
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
    void addBooking() {
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.getById(item.getId())).thenReturn(item);
        Booking result = service.addBooking(2,request);
        assertEquals(booking,result);
        assertEquals(request.getItemId(),result.getItem().getId());
        assertEquals(item,result.getItem());
        assertEquals(booker,result.getBooker());
        verify(bookingRepository,times(1)).save(Mockito.any(Booking.class));
    }

    @Test
    void addBookingWrongTime(){
        request.setEnd(LocalDateTime.now().minusHours(1));
        assertThrows(ValidationException.class,() ->service.addBooking(2,request));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));

    }

    @Test
    void addBookingWrongUser(){
        assertThrows(StorageException.class,() ->service.addBooking(5,request));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));
        assertThrows(StorageException.class,() ->service.addBooking(item.getOwner().getId(),request));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));

    }

    @Test
    void addBookingNotAvailableItem(){
        item.setAvailable(false);
        assertThrows(StorageException.class, () -> service.addBooking(2,request));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));
    }
//
//        ArgumentCaptor<Booking> argument = ArgumentCaptor.forClass(Booking.class);
//        verify(bookingRepository,times(1).save(argument));
//        assertEquals(booking.getId(), argument.getValue().getId());



    @Test
    void getStatus() {
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        Booking result = service.getStatus(booking.getId(), owner.getId(),true);
        assertEquals(BookStatus.APPROVED,result.getStatus());
        assertEquals(booking.getId(),result.getId());
        assertEquals(booking.getItem(),result.getItem());
    }

    @Test
    void getStatusWrongUser(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        assertThrows(StorageException.class,() ->service.getStatus(booking.getId(), 5,true));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));
        assertThrows(StorageException.class,() ->service.getStatus(booking.getId(), booker.getId(), true));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));
    }

    @Test
    void getStatusWithApproved(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        booking.setStatus(BookStatus.APPROVED);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        assertThrows(StorageException.class,() -> service.getStatus(booking.getId(), owner.getId(),true));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));
    }

    @Test
    void getStatusNotApproved(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        Booking result = service.getStatus(booking.getId(), owner.getId(),false);
        assertEquals(BookStatus.REJECTED,result.getStatus());
        assertEquals(booking.getId(),result.getId());
        assertEquals(booking.getItem(),result.getItem());
    }

    @Test
    void getBooking() {
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        Booking result = service.getBooking(1,owner.getId());
        assertEquals(booking,result);
        assertEquals(booking.getId(),result.getId());
        assertEquals(booking.getItem(),result.getItem());
        when(userRepository.existsById(booker.getId())).thenReturn(true);
        Booking result2 = service.getBooking(1,booker.getId());
        assertEquals(booking,result2);
        assertEquals(booking.getId(),result2.getId());
        assertEquals(booking.getItem(),result2.getItem());
    }

    @Test
    void getBookingWrongUser(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        User user = new User(3,"user2@book.ru","user2");
        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(userRepository.existsById(user.getId())).thenReturn(true);
        assertThrows(ChangeException.class,() -> service.getBooking(booking.getId(), user.getId()));
        verify(bookingRepository,never()).save(Mockito.any(Booking.class));
    }

    @Test
    void getUserBookingsAll() {
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerOrderByStartDesc(booker, PageRequest.of(1,1))).thenReturn(List.of(booking));
       List<BookingResponse> result = service.getUserBookings(booker.getId(), "ALL",1,1);
       assertTrue(result.contains(BookingMapper.toResponse(booking)));
       assertEquals(1,result.size());
       verify(bookingRepository,times(1)).findAllByBookerOrderByStartDesc(any(),any());
       verify(bookingRepository,never()).findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class),any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getUserBookingsCURRENT(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class),any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "CURRENT",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class),any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository,never()).findAllByBookerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserBookingsFUTURE(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(any(User.class),any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "FUTURE",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByBookerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository,never()).findAllByBookerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserBookingsPAST(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(any(User.class),any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "PAST",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByBookerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository,never()).findAllByBookerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserBookingsWAITING(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(booker,BookStatus.WAITING, PageRequest.of(1,1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "WAITING",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByBookerAndStatusEqualsOrderByStartDesc(booker,BookStatus.WAITING, PageRequest.of(1,1));
        verify(bookingRepository,never()).findAllByBookerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserBookingsREJECTED(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.REJECTED);
        booking.setId(1);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(booker,BookStatus.REJECTED, PageRequest.of(1,1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserBookings(booker.getId(), "REJECTED",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByBookerAndStatusEqualsOrderByStartDesc(booker,BookStatus.REJECTED, PageRequest.of(1,1));
        verify(bookingRepository,never()).findAllByBookerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserBookingWrongState(){
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        assertThrows(ValidationException.class,() -> service.getUserBookings(booker.getId(), " ",1,1));
        verify(bookingRepository,never()).findAllByBookerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserItemsALL() {
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerOrderByStartDesc(owner, PageRequest.of(1,1))).thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "ALL",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByItem_OwnerOrderByStartDesc(any(),any());
        verify(bookingRepository,never()).findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class),any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getUserItemsCURRENT(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class),any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "CURRENT",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(any(User.class), any(LocalDateTime.class),any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository,never()).findAllByItem_OwnerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserItemsFUTURE(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(any(User.class),any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "FUTURE",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository,never()).findAllByItem_OwnerOrderByStartDesc(any(),any());
    }
    //
    @Test
    void getUserItemsPAST(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.APPROVED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(any(User.class),any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "PAST",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        verify(bookingRepository,never()).findAllByItem_OwnerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserItemsWAITING(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.WAITING);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner,BookStatus.WAITING, PageRequest.of(1,1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "WAITING",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner,BookStatus.WAITING, PageRequest.of(1,1));
        verify(bookingRepository,never()).findAllByItem_OwnerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserItemsREJECTED(){
        Booking booking = BookingMapper.toBooking(request, item, booker,BookStatus.REJECTED);
        booking.setId(1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner,BookStatus.REJECTED, PageRequest.of(1,1)))
                .thenReturn(List.of(booking));
        List<BookingResponse> result = service.getUserItems(owner.getId(), "REJECTED",1,1);
        assertTrue(result.contains(BookingMapper.toResponse(booking)));
        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner,BookStatus.REJECTED, PageRequest.of(1,1));
        verify(bookingRepository,never()).findAllByItem_OwnerOrderByStartDesc(any(),any());
    }

    @Test
    void getUserItemsWrongState(){
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        assertThrows(ValidationException.class,() -> service.getUserItems(owner.getId(), " ",1,1));
        verify(bookingRepository,never()).findAllByItem_OwnerOrderByStartDesc(any(),any());
    }



}