package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithTime;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, ItemDto item);

    Item updateItem(long id, long userId, ItemDto item);

    ItemWithTime getItem(long id, long userId);

    List<ItemWithTime> getUserItems(long userId);

    List<Item> searchItem(String text);
}
