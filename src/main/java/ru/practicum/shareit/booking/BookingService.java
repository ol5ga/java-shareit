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
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@Builder
public class BookingService {
    BookingStorage storage;
    UserStorage userStorage;

    ItemRepository itemStorage;

    public Booking addBooking (long userId, BookingRequest request) throws ValidationException {
        if (request.getStart().isAfter(request.getEnd()) ||
                request.getStart().isEqual(request.getEnd()))
            throw new ValidationException("Некоректно указан интервал бронирования");
        checkUser(userId);
        User user = userStorage.getById(userId);
        Item item = itemStorage.getById(request.getItemId());
        if(userId == item.getOwner().getId()){
            throw new ChangeException("Собственник не может забронировать свою вещь");
        }
        if(!item.getAvailable()){
            throw new ValidationException("Эта вещь недоступна для бронирования");
        }
        BookStatus status = BookStatus.WAITING;
        return storage.save(BookingMapper.toBooking(request,item,user,status));
    }

    public Booking getStatus (long bookingId, long userId, Boolean approved){
        Booking booking = storage.getById(bookingId);
        Item item = booking.getItem();
        checkUser(userId);
        if (userId != item.getOwner().getId()){
            throw new ChangeException("Операцию может выполнить только владелец");
        }
        if (approved){
            if(booking.getStatus() == BookStatus.APPROVED){
                throw new ValidationException("Заявка уже одобобрена");
            }
            booking.setStatus(BookStatus.APPROVED);
        } else {
            booking.setStatus(BookStatus.REJECTED);
        }
        storage.save(booking);
        return storage.getById(bookingId);
    }

    public Booking getBooking (long bookingId, long userId) {
        Booking booking = storage.getById(bookingId);
        Item item = booking.getItem();
        checkUser(userId);
        if (userId != item.getOwner().getId() && userId != booking.getBooker().getId()){
            throw new ChangeException("Нет прав на получение информации");
        }
        return storage.getById(bookingId);
    }

    public List<Booking> getUserBookings (long userId, String status){
        checkUser(userId);
        User user = userStorage.getById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();

        switch (status) {
            case "ALL":
                bookings = storage.findAllByBookerOrderByStartDesc(user);
                break;
            case "CURRENT":
                bookings = storage.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(user, now, now);
                break;
            case "FUTURE":
                bookings = storage.findAllByBookerAndStartIsAfterOrderByStartDesc(user, now);
                break;
            case "PAST":
                bookings = storage.findAllByBookerAndEndIsBeforeOrderByStartDesc(user, now);
                break;
            case "WAITING":
                bookings = storage.findAllByBookerAndStatusEqualsOrderByStartDesc(user, BookStatus.WAITING);
                break;
            case "REJECTED":
                bookings = storage.findAllByBookerAndStatusEqualsOrderByStartDesc(user, BookStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");

        }
        return bookings;
    }

    public List<Booking> getUserItems(long userId, String status) {
        checkUser(userId);
        User user = userStorage.getById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();

        switch (status) {
            case "ALL":
                bookings = storage.findAllByItem_OwnerOrderByStartDesc(user);
                break;
            case "CURRENT":
                bookings = storage.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(user, now, now);
                break;
            case "FUTURE":
                bookings = storage.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(user, now);
                break;
            case "PAST":
                bookings = storage.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(user, now);
                break;
            case "WAITING":
                bookings = storage.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(user, BookStatus.WAITING);
                break;
            case "REJECTED":
                bookings = storage.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(user, BookStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + status);

        }
        return bookings;
    }

    private void checkUser(long userId){
        if(!userStorage.existsById(userId)){
            throw new StorageException("Такого пользователя не существует");
        }
    }
}
