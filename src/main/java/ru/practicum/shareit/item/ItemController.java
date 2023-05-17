package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto item){
        return
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable long id){
        return
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable long id){
        return
    }

    @GetMapping
    public List<ItemDto> getUserItems(){
        return
    }

    @GetMapping("/search?text={text}")
    public List<ItemDto> searchItem(){
        return
    }
}
