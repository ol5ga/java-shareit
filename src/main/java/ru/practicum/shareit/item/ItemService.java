package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ItemService {

    ItemStorage storage;

    public Item addItem(long userId, Item item){
        return storage.addItem(userId, item);
    }

    public Item updateItem(long id, long userId){
        return storage.updateItem(id, userId);
    }

    public Item getItem(long id, long userId){
        return storage.getItem(id, userId);
    }
    public List<Item> getUserItems(long userId){
        return storage.getUserItems(userId);
    }

    public List<Item> searchItem(String text){
        return storage.searchItem(text);
    }
}
