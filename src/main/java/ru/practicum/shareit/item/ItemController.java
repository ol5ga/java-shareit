package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.annotation.ItemCreate;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithProperty;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    private static final String USER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER) Long userId, @Validated(ItemCreate.class) @RequestBody ItemDto itemDto) {
        Item item = service.addItem(userId, itemDto);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable long id, @RequestBody ItemDto itemDto, @RequestHeader(USER) long userId) {
        Item item = service.updateItem(id, userId, itemDto);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/{id}")
    public ItemWithProperty getItem(@PathVariable long id, @RequestHeader(USER) long userId) {
        return service.getItem(id, userId);
    }

    @GetMapping
    public List<ItemWithProperty> getUserItems(@RequestHeader(USER) long userId, @RequestParam(name = "from", defaultValue = "1") @Min(1) Integer from, @RequestParam(name = "size",defaultValue = "20") @Min(1) @Max(20) Integer size) {
        return service.getUserItems(userId, from, size);
    }


    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(name = "text") String text, @RequestParam(name = "from", defaultValue = "1") @Min(1) Integer from, @RequestParam(name = "size",defaultValue = "20") @Min(1) @Max(20) Integer size) {
        return service.searchItem(text, from, size).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse addComment(@RequestHeader(USER) long userId, @PathVariable long itemId, @Valid @RequestBody CommentRequest commentRequest) {
        return CommentMapper.toResponse(service.addComment(userId, itemId, commentRequest));
    }
}
