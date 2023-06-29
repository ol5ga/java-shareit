package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithProperty;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, ItemDto item);

    Item updateItem(long id, long userId, ItemDto item);

    ItemWithProperty getItem(long id, long userId);

    List<ItemWithProperty> getUserItems(long userId, int from, int size);

    List<Item> searchItem(String text, int from, int size);

    Comment addComment(long userId, long itemId, CommentRequest text);
}
