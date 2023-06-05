package ru.practicum.shareit.booking;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class BookingService {
    BookingStorage storage;

    public Booking addBooking (long userId, Booking booking){
        return storage.save(booking);
    }

    public Booking getStatus (long bookingId, long userId, Boolean approved){
        return null; //storage.getStatus(bookingId, userId, approved);
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
