package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item);

    Item updateItem(long userId, Item updateItem);

    Item getItem(long id);

    List<Item> getUserItems(long userId);

    List<Item> searchItem(String text);
}
