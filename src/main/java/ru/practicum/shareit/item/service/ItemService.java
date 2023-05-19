package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, Item item);

    Item updateItem(long userId, Item item);

    Item getItem(long id, long userId);

    List<Item> getUserItems(long userId);

    List<Item> searchItem(String text);
}
