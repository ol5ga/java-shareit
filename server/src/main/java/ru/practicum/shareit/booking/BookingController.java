package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Validated
public class BookingController {

    private BookingService service;
    private static final String USER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponse addBooking(@RequestHeader(USER) long userId, @Valid @RequestBody BookingRequest request) {
        return service.addBooking(userId, request);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse getStatus(@PathVariable long bookingId, @RequestHeader(USER) long userId, @RequestParam Boolean approved) {
        return service.getStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable long bookingId, @RequestHeader(USER) long userId) {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponse> getUserBookings(@RequestHeader(USER) long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @Valid @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                 @Valid @RequestParam(defaultValue = "20") @Min(1) @Max(20) Integer size) {
        LocalDateTime now = LocalDateTime.now();
        return service.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getUserItems(@RequestHeader(USER) long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "20") @Min(1) @Max(20) Integer size) {
        return service.getUserItems(userId, state, from, size);
    }
}
