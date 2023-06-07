package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithTime;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository storage;
    private final UserStorage userStorage;

    private final BookingStorage bookStorage;

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
        try { storage.getById(id);
        } catch (EntityNotFoundException ex){
            log.warn("Неправильный id");
            throw new StorageException("Такой вещи не существует");
        }
         if (item.getName() == null) {
                item.setName(oldItem.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(oldItem.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(oldItem.getAvailable());
            }
        storage.save(item);
        return storage.getById(item.getId());
    }


    @Override
    public ItemWithTime getItem(long id, long userId) {
        checkUser(userId);
        return addBooking(storage.getById(id));
    }

    @Override
    public List<ItemWithTime> getUserItems(long userId) {
        User user = userStorage.getById(userId);
        return storage.findAllByOwner(user).stream()
                .map(item -> addBooking(item))
                .collect(Collectors.toList());
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
        if (!userStorage.existsById(userId)){

            throw new ChangeException("Такого пользователя не существует");
        }
    }

    private ItemWithTime addBooking(Item item){
        LocalDateTime now = LocalDateTime.now();
        Booking lastBook = bookStorage.findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(item.getId(),now,now);
        Booking nextBook = bookStorage.findFirstByItemIdAndStartIsAfterOrderByStart(item.getId(),now);
        BookingShort last;
        BookingShort next;
        if(lastBook != null) {
            last = BookingMapper.toBookingShort(lastBook);
        } else {
            last = null;
        }
        if (nextBook != null){
        next = BookingMapper.toBookingShort(nextBook);
        } else {
            next = null;
        }
        return ItemMapper.toItemWithTime(item,last,next);
    }
}
