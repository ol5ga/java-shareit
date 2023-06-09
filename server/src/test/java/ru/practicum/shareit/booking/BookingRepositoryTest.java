package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private Item item2;
    private User booker;
    private User booker2;
    private User owner;
    private User owner2;

    private Booking booking;
    private Booking booking2;

    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        owner2 = User.builder()
                .email("owner2@Mail.ru")
                .name("owner2")
                .build();
        userRepository.save(owner);
        userRepository.save(owner2);
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        item2 = Item.builder()
                .name("name2")
                .description("item2")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);
        itemRepository.save(item2);
        booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        booker2 = User.builder()
                .email("booker2@mail.ru")
                .name("booker2")
                .build();
        userRepository.save(booker);
        userRepository.save(booker2);
        booking = Booking.builder()
                .start(now)
                .end(now.plusDays(3))
                .item(item)
                .booker(booker)
                .status(BookStatus.APPROVED)
                .build();
        booking2 = Booking.builder()
                .start(now.plusHours(2))
                .end(now.plusDays(2))
                .item(item2)
                .booker(booker)
                .status(BookStatus.APPROVED)
                .build();

        bookingRepository.save(booking);
        bookingRepository.save(booking2);
    }

    @Test
    void testGettingUsersBookingDesc() {
        List<Booking> result = bookingRepository.findAllByBookerOrderByStartDesc(booker, Pageable.unpaged());
        assertEquals(2, result.size());
        assertThat(result.contains(booking));
        assertEquals(booking2, result.get(0));
        assertEquals(booking, result.get(1));
    }

    @Test
    void testGettingUsersBookingInInterval() {
        List<Booking> result = bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(booker, now.plusMinutes(5), now.plusMinutes(5), Pageable.unpaged());
        assertEquals(1, result.size());
        assertThat(result.contains(booking));
    }

    @Test
    void testGettingUsersBookingStartAfterTime() {
        List<Booking> result = bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(booker, now.minusHours(5), Pageable.unpaged());
        assertEquals(2, result.size());
        assertThat(result.contains(booking));
        assertEquals(booking2, result.get(0));
        assertEquals(booking, result.get(1));
    }

    @Test
    void testGettingUsersBookingEndBeforeTime() {
        List<Booking> result = bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(booker, now.plusDays(5), Pageable.unpaged());
        assertEquals(2, result.size());
        assertThat(result.contains(booking));
        assertEquals(booking2, result.get(0));
        assertEquals(booking, result.get(1));
    }

    @Test
    void testGettingUsersBookingsWithStatus() {
        booking.setStatus(BookStatus.WAITING);
        List<Booking> result = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(booker, BookStatus.WAITING, Pageable.unpaged());
        assertEquals(1, result.size());
        assertThat(result.contains(booking));
    }

    @Test
    void testGettingOwnersBookingsDesc() {
        List<Booking> result = bookingRepository.findAllByItem_OwnerOrderByStartDesc(owner, Pageable.unpaged());
        assertEquals(2, result.size());
        assertThat(result.contains(booking));
        assertEquals(booking2, result.get(0));
        assertEquals(booking, result.get(1));
    }

    @Test
    void testGettingOwnersBookingInInterval() {
        List<Booking> result = bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(owner, now.plusMinutes(5), now.plusMinutes(5), Pageable.unpaged());
        assertEquals(1, result.size());
        assertThat(result.contains(booking));
    }

    @Test
    void testGettingUsersBookingStartIsAfterTime() {
        List<Booking> result = bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(owner, now.minusHours(5), Pageable.unpaged());
        assertEquals(2, result.size());
        assertThat(result.contains(booking));
        assertEquals(booking2, result.get(0));
        assertEquals(booking, result.get(1));
    }

    @Test
    void testGettingUsersBookingEndIsBeforeTime() {
        List<Booking> result = bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(owner, now.plusDays(5), Pageable.unpaged());
        assertEquals(2, result.size());
        assertThat(result.contains(booking));
        assertEquals(booking2, result.get(0));
        assertEquals(booking, result.get(1));
    }

    @Test
    void testGettingUsersBookingInIntervalWithStatus() {
        booking2.setStatus(BookStatus.WAITING);
        List<Booking> result = bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner, BookStatus.WAITING, Pageable.unpaged());
        assertEquals(1, result.size());
        assertThat(result.contains(booking2));
    }

    @Test
    void testFindNowBooking() {
        Booking result = bookingRepository.findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(item.getId(), now.plusHours(1), now.plusHours(1));
        assertEquals(booking, result);
    }

    @Test
    void testFindNextBooking() {
        Booking result = bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStart(item.getId(), now.minusHours(1));
        assertEquals(booking, result);
    }


    @Test
    void testFindLastBooking() {
        Booking result = bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(booker.getId(), item2.getId(), now.plusDays(5));
        assertEquals(booking2, result);
    }
}