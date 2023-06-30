package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER) long userId,
                                             @RequestBody @Valid BookingRequest request) {
        log.info("Create booking by user id {}", userId);
        return bookingClient.addBooking(userId, request);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> getStatus(@RequestHeader(name = USER) long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        if (approved) {
            log.info("Accept booking id {} by user id {}", bookingId, userId);
        } else {
            log.info("Decline booking id {} by user id {}", bookingId, userId);
        }
        return bookingClient.getStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(name = USER) long userId,
                                             @PathVariable long bookingId) {
        log.info("Get booking id {} by user id {}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUserBookings(@RequestHeader(name = USER) long userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @PositiveOrZero @RequestParam(required = false) Integer from,
                                                  @Positive @RequestParam(required = false) Integer size) {
        log.info("Get all user bookings state {} user id {} from {} size {}", state, userId, from, size);
        return bookingClient.getUserBookings(userId, state, from, size, false);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItems(@RequestHeader(name = USER) long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @PositiveOrZero @RequestParam(required = false) Integer from,
                                               @Positive @RequestParam(required = false) Integer size) {
        log.info("Get all owner bookings state {} user id {} from {} size {}", state, userId, from, size);
        return bookingClient.getUserBookings(userId, state, from, size, true);
    }
}