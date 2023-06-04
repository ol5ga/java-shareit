package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository storage;
    private final UserRepository userStorage;

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        checkUser(userId);
        User user = userStorage.getById(userId);
        Item item = ItemMapper.toItem(itemDto.getId(),user, itemDto);
        return storage.save(item);
    }

    @Override
    public Item updateItem(long id, long userId, ItemDto itemDto) {
        checkUser(userId);
        if (userId != storage.getById(id).getOwner().getId()) {
            throw new ChangeException("Изменения может вносить только владелец");
        }
        User user = userStorage.getById(userId);
        Item item = ItemMapper.toItem(id,user, itemDto);
        Item oldItem = storage.getById(id);
        if (storage.getById(id) != null) {
            log.warn("Неправильный id");
            throw new StorageException("Такой вещи не существует");
        } else {

            if (item.getName() == null) {
                item.setName(oldItem.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(oldItem.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(oldItem.getAvailable());
            }
        }
        storage.delete(item);
        storage.save(item);
        return item;
    }


    @Override
    public Item getItem(long id, long userId) {
        checkUser(userId);
        return storage.getById(id);
    }

    @Override
    public List<Item> getUserItems(long userId) {
        User user = userStorage.getById(userId);
        return storage.findAllByOwner(user);
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return storage.findAll().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    private void checkUser(long userId) {
        try {
            userStorage.getById(userId);
        } catch (StorageException ex) {
            throw new ChangeException("Такого пользователя не существует");
        }
    }
}
