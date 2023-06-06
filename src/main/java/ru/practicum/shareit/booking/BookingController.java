package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
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
        public BookingResponse addBooking (@RequestHeader(USER) long userId,@Valid @RequestBody BookingRequest request){
            Booking booking = service.addBooking(userId,request);
            return BookingMapper.toResponse(booking);
        }

        @PatchMapping("/{bookingId}")
        public BookingResponse getStatus (@PathVariable long bookingId, @RequestHeader(USER) long userId,@RequestParam(name = "approved") Boolean approved){
            Booking booking = service.getStatus(bookingId, userId, approved);
            return BookingMapper.toResponse(booking);
        }
        @GetMapping("/{bookingId}")
        public BookingResponse getBooking (@PathVariable long bookingId, @RequestHeader(USER) long userId) {
            Booking booking = service.getBooking(bookingId, userId);
            return BookingMapper.toResponse(booking);
        }

        @GetMapping
        public List<Booking> getUserBookings (@RequestHeader(USER) long userId,@RequestParam(defaultValue = "ALL") BookStatus status){
            return service.getUserBookings(userId, status);
        }

        @GetMapping("/owner")
        public List<Booking> getUserItems (@PathVariable long userId, @RequestParam(defaultValue = "ALL") BookStatus status){
            return service.getUserItems(userId,status);
        }
}
