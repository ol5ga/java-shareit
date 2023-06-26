package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

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
    public BookingResponse getStatus(@PathVariable long bookingId, @RequestHeader(USER) long userId, @RequestParam(name = "approved") Boolean approved) {
        return service.getStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable long bookingId, @RequestHeader(USER) long userId) {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponse> getUserBookings(@RequestHeader(USER) long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                 @Valid @RequestParam(name = "from", defaultValue = "1") @Min(1) Integer from,
                                                 @Valid @RequestParam(name = "size",defaultValue = "20") @Min(1) @Max(20) Integer size) {
        return service.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getUserItems(@RequestHeader(USER) long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestParam(name = "from", defaultValue = "1") @Min(1) Integer from,
                                              @RequestParam(name = "size",defaultValue = "20") @Min(1) @Max(20) Integer size) {
        return service.getUserItems(userId, state,from, size);
    }
}
