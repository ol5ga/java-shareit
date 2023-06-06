package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

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
        if(!userStorage.existsById(userId)){
            throw new StorageException("Такого пользователя не существует");
        }
        User user = userStorage.getById(userId);
        Item item = itemStorage.getById(request.getItemId());
        if(!item.getAvailable()){
            throw new ValidationException("Эта вещь недоступна для бронирования");
        }
        BookStatus status = BookStatus.WAITING;
        return storage.save(BookingMapper.toBooking(request,item,user,status));
    }

    public Booking getStatus (long bookingId, long userId, Boolean approved){
        Booking booking = storage.getById(bookingId);
        Item item = booking.getItem();
        if (userId != item.getOwner().getId()){
            throw new StorageException("Операцию может выполнить только владелец");
        }
        if (approved){
            booking.setStatus(BookStatus.APPROVED);
        } else {
            booking.setStatus(BookStatus.REJECTED);
        }
        return booking;
    }

    public Booking getBooking (long bookingId, long userId) {

        return storage.getById(bookingId);
    }

    public List<Booking> getUserBookings (long userId, BookStatus status){
        return null; //storage.getUserBookings(userId,status);
    }

    public List<Booking> getUserItems(long userId, BookStatus status) {

        return null; //storage.getUserItems(userId,status);
    }
}
