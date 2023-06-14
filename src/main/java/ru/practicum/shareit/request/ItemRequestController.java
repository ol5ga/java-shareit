package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
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
    public List<ItemRequest> getMyRequests(@RequestHeader(USER) long userId){
        return service.getMyRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllRequests(@RequestHeader(USER) long userId, @RequestParam(name = "from") int from, @RequestParam(name = "size") int size){
        return service.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequest getItemRequest(@PathVariable long requestId, @RequestHeader(USER) long userId){
        return service.getRequest();
    }


}
