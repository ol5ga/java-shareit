package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage storage;
    private final UserStorage userStorage;

    public Item addItem(long userId, Item item){
        try { userStorage.getUser(userId);
        } catch (StorageException ex) {
            throw new ChangeException("Такого пользователя не существует");
        }
        return storage.addItem(item);
    }

    public Item updateItem(Item item){
        try { userStorage.getUser(item.getOwner());
        } catch (StorageException ex) {
            throw new ChangeException("Такого пользователя не существует");
        }
        return storage.updateItem(item);
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
