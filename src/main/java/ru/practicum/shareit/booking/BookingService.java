package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@Builder
public class BookingService {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;

    private ItemRepository itemRepository;

    @Transactional
    public Booking addBooking(long userId, BookingRequest request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();
        if (start.isAfter(end) || start.isEqual(end))
            throw new ValidationException("Некоректно указан интервал бронирования");
        checkUser(userId);
        User user = userRepository.getById(userId);
        Item item = itemRepository.getById(request.getItemId());
        if (userId == item.getOwner().getId()) {
            throw new ChangeException("Собственник не может забронировать свою вещь");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Эта вещь недоступна для бронирования");
        }
        BookStatus status = BookStatus.WAITING;
        return bookingRepository.save(BookingMapper.toBooking(request, item, user, status));
    }

    @Transactional
    public Booking getStatus(long bookingId, long userId, Boolean approved) {
        Booking booking = bookingRepository.getById(bookingId);
        Item item = booking.getItem();
        checkUser(userId);
        if (userId != item.getOwner().getId()) {
            throw new ChangeException("Операцию может выполнить только владелец");
        }
        if (approved) {
            if (booking.getStatus() == BookStatus.APPROVED) {
                throw new ValidationException("Заявка уже одобобрена");
            }
            booking.setStatus(BookStatus.APPROVED);
        } else {
            booking.setStatus(BookStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingRepository.getById(bookingId);
    }

    public Booking getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.getById(bookingId);
        Item item = booking.getItem();
        checkUser(userId);
        if (userId != item.getOwner().getId() && userId != booking.getBooker().getId()) {
            throw new ChangeException("Нет прав на получение информации");
        }
        return bookingRepository.getById(bookingId);
    }

    public List<Booking> getUserBookings(long userId, String status) {
        checkUser(userId);
        User user = userRepository.getById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();

        switch (status) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerOrderByStartDesc(user);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(user, now, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(user, now);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(user, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, BookStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, BookStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");

        }
        return bookings;
    }

    public List<Booking> getUserItems(long userId, String status) {
        checkUser(userId);
        User user = userRepository.getById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();

        switch (status) {
            case "ALL":
                bookings = bookingRepository.findAllByItem_OwnerOrderByStartDesc(user);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(user, now, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(user, now);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(user, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(user, BookStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(user, BookStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + status);

        }
        return bookings;
    }

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new StorageException("Такого пользователя не существует");
        }
    }
}
