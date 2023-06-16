package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private static final String USER = "X-Sharer-User-Id";
    private ItemRequestService service;

    @PostMapping
    public ItemRequest addRequest(@RequestHeader(USER) long userId, @Valid @RequestBody ItemRequestDto request){
        LocalDateTime created = LocalDateTime.now();
        return service.addRequest(userId,request,created);
    }

    @GetMapping
    public List<ItemRequestResponse> getMyRequests(@RequestHeader(USER) long userId){
        return service.getMyRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponse> getAllRequests(@RequestHeader(USER) long userId,
                                                    @RequestParam(name = "from", defaultValue = "1") @Min(1) Integer from,
                                                    @RequestParam(name = "size",defaultValue = "20") @Min(1) @Max(20) Integer size){
        return service.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponse getItemRequest(@PathVariable long requestId, @RequestHeader(USER) long userId){
        return service.getRequest(requestId, userId);
    }


}
