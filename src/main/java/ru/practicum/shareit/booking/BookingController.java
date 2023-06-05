package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
    @RestController
    @RequestMapping(path = "/bookings")
    @AllArgsConstructor
    public class BookingController {

        BookingService service;
        static final String USER = "X-Sharer-User-Id";
        @PostMapping
        public Booking addBooking (@RequestHeader(USER) long userId, @RequestBody Booking booking){
            return service.addBooking(userId,booking);
        }

        @PatchMapping("/{bookingId}")
        public Booking getStatus (@PathVariable long bookingId, @RequestHeader(USER) long userId,@RequestParam(name = "approved") Boolean approved){
            return service.getStatus(bookingId, userId, approved);
        }
        @GetMapping("/{bookingId}")
        public Booking getBooking (@PathVariable long bookingId, @RequestHeader(USER) long userId) {
            return service.getBooking(bookingId,userId);
        }

        @GetMapping
        public List<Booking> getUserBookings (@RequestHeader(USER) long userId,@RequestParam(defaultValue = "ALL") BookStatus status){
            return service.getUserBookings(userId, status);
        }

        @GetMapping("/{owner}")
        public List<Booking> getUserItems (@PathVariable long userId, @RequestParam(defaultValue = "ALL") BookStatus status){
            return service.getUserItems(userId,status);
        }
}
