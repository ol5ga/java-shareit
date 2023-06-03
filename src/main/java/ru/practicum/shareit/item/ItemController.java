package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.annotation.ItemCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    static final String USER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER) long userId, @Validated(ItemCreate.class) @RequestBody ItemDto itemDto) {
        Item item = service.addItem(userId, itemDto);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable long id, @RequestBody ItemDto itemDto, @RequestHeader(USER) long userId) {
        Item item = service.updateItem(id,userId, itemDto);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable long id, @RequestHeader(USER) long userId) {
        Item item = service.getItem(id, userId);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(USER) long userId) {
        return service.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(name = "text") String text) {
        return service.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
