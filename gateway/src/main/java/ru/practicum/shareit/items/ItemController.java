package ru.practicum.shareit.items;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.items.annotation.ItemCreate;
import ru.practicum.shareit.items.dto.ItemDto;
import ru.practicum.shareit.comment.CommentRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER) long userId,
                                          @Validated(ItemCreate.class) @Valid @RequestBody ItemDto itemUserDto) {
        log.info("Create new item userid {} name {}", userId, itemUserDto.getName());
        return itemClient.addItem(userId, itemUserDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                             @RequestHeader(USER) long userId,
                                             @Valid @RequestBody ItemDto itemUserDto) {
        log.info("Update item id {}", itemId);
        return itemClient.updateItem(userId, itemId, itemUserDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(USER) long userId) {
        log.info("Get item id {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(USER) long userId) {
        log.info("Get user {} items", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(USER) long userId,
                                             @RequestParam(name = "text") String text,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Search item text {}", text);
        return itemClient.findItem(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader(name = USER) long userId,
                                              @PathVariable long itemId,
                                              @Valid @RequestBody CommentRequest commentRequest) {
        log.info("Post comment userid {} itemid {}", userId, itemId);
        return itemClient.postComment(userId, itemId, commentRequest);
    }
}