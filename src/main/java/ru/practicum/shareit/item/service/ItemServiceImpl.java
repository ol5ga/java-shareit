package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage storage;
    private final UserStorage userStorage;

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        checkUser(userId);
        User user = userStorage.getUser(userId);
        Item item = ItemMapper.toItem(user, itemDto);
        return storage.addItem(item);
    }

    @Override
    public Item updateItem(long id, long userId, ItemDto itemDto) {
        checkUser(userId);
        if (userId != storage.getItem(id).getOwner().getId()) {
            throw new ChangeException("Изменения может вносить только владелец");
        }
        User user = userStorage.getUser(userId);
        Item item = ItemMapper.toItem(id,user, itemDto);

        return storage.updateItem(userId, item);
    }

    @Override
    public Item getItem(long id, long userId) {
        checkUser(userId);
        return storage.getItem(id);
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return storage.getUserItems(userId);
    }

    @Override
    public List<Item> searchItem(String text) {
        return storage.searchItem(text);
    }

    private void checkUser(long userId) {
        try {
            userStorage.getUser(userId);
        } catch (StorageException ex) {
            throw new ChangeException("Такого пользователя не существует");
        }
    }
}
