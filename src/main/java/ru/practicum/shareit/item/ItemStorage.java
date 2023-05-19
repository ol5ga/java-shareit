package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemStorage {

    Map<Long, Item> items = new HashMap<>();
    long id = 0;

    public Item addItem(Item item){
        id++;
        item.setId(id);
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(long userId, Item updateItem){
        Item oldItem = items.get(updateItem.getId());
        if (userId != oldItem.getOwner()){
            throw new ChangeException("Изменеия может вносить только владелец");
        }
        if (!items.containsKey(updateItem.getId())) {
            log.info("Неправильный id");
            throw new StorageException("Такой вещи не существует");
        } else {

            if(updateItem.getName() == null){
                updateItem.setName(oldItem.getName());
            }
            if(updateItem.getDescription() == null){
                updateItem.setDescription(oldItem.getDescription());
            }
            if(updateItem.getAvailable() == null){
                updateItem.setAvailable(oldItem.getAvailable());
            }
        }
        items.remove(oldItem.getId());
        items.put(updateItem.getId(), updateItem);
        return updateItem;
    }

    public Item getItem(long id){
        if (!items.containsKey(id)) {
            log.info("Неправильный id");
            throw new StorageException("вещи с таким id не существует");
        }
        return items.get(id);
    }
    public List<Item> getUserItems(long userId){
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner() == userId){
                userItems.add(item);
            }
        }
        return userItems;
    }

    public List<Item> searchItem(String text){
        if (text.isEmpty()){
            return new ArrayList<>();
        }
       return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}
