package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated(ItemCreate.class) @RequestBody ItemDto itemDto){
        Item item = service.addItem(userId, ItemMapper.toItem(userId, itemDto));
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable long id,  @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId){
        Item item = service.updateItem(userId,ItemMapper.toItem(id, userId,itemDto));
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId){
        Item item = service.getItem(id,userId);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId){
        return service.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(name = "text") String text){
        return service.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
