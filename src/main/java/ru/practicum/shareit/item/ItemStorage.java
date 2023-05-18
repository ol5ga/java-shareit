package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public Item updateItem(Item updateItem){
        Item oldItem = items.get(updateItem.getId());
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

    public Item getItem(long id, long userId){
        return null;
    }
    public List<Item> getUserItems(long userId){
        return null;
    }

    public List<Item> searchItem(String text){
        return null;
    }
}
