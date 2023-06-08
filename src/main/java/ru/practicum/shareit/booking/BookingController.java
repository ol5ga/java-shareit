package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    BookingService service;
    static final String USER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponse addBooking(@RequestHeader(USER) long userId, @Valid @RequestBody BookingRequest request) {
        Booking booking = service.addBooking(userId, request);
        return BookingMapper.toResponse(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse getStatus(@PathVariable long bookingId, @RequestHeader(USER) long userId, @RequestParam(name = "approved") Boolean approved) {
        Booking booking = service.getStatus(bookingId, userId, approved);
        return BookingMapper.toResponse(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable long bookingId, @RequestHeader(USER) long userId) {
        Booking booking = service.getBooking(bookingId, userId);
        return BookingMapper.toResponse(booking);
    }

    @GetMapping
    public List<BookingResponse> getUserBookings(@RequestHeader(USER) long userId, @RequestParam(name = "state", defaultValue = "ALL") String state) {
        List<Booking> bookings = service.getUserBookings(userId, state);
        return bookings.stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingResponse> getUserItems(@RequestHeader(USER) long userId, @RequestParam(name = "state", defaultValue = "ALL") String state) {
        List<Booking> bookings = service.getUserItems(userId, state);
        return bookings.stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }
}
