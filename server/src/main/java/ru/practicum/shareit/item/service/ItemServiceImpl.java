package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithProperty;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookRepository;
    private final CommentRepository commentRepository;

    private final ItemRequestRepository requestRepository;

    @Override
    @Transactional
    public Item addItem(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Такого пользователя не существует"));
        Item item;
        if (itemDto.getRequestId() != null) {
            ItemRequest request = requestRepository.findById(itemDto.getRequestId()).orElseThrow();
            item = ItemMapper.toItem(itemDto.getId(), user, itemDto, request);
        } else {
            item = ItemMapper.toItem(itemDto.getId(), user, itemDto);
        }
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(long id, long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Такого пользователя не существует"));
        if (userId != itemRepository.getById(id).getOwner().getId()) {
            throw new ChangeException("Изменения может вносить только владелец");
        }

        Item item = ItemMapper.toItem(id, user, itemDto);
        Item oldItem = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такой вещи не существует"));
        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        itemRepository.save(item);
        return itemRepository.getById(item.getId());
    }


    @Override
    public ItemWithProperty getItem(long id, long userId) {
        checkUser(userId);
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такой вещи не существует"));
        return addProperty(item, userId);
    }

    @Override
    public List<ItemWithProperty> getUserItems(long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Такого пользователя не существует"));
        Pageable page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<ItemWithProperty> items = itemRepository.findByOwnerId(userId, page).stream()
                .map(item -> addProperty(item, userId))
                .collect(Collectors.toList());
        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItem(String text, int from, int size) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        Pageable page = PageRequest.of(from / size, size);
        return itemRepository.search(text, page).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Comment addComment(long userId, long itemId, CommentRequest request) {
        Item item = itemRepository.getById(itemId);
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Такого пользователя не существует"));
        Comment comment;
        if (bookRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(userId, itemId, LocalDateTime.now()) != null) {
            comment = CommentMapper.toComment(request, item, user, LocalDateTime.now());
        } else {
            throw new ValidationException("Пользователь не может оставить коментарий");
        }
        return commentRepository.save(comment);
    }

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ChangeException("Такого пользователя не существует");
        }
    }

    private ItemWithProperty addProperty(Item item, long userId) {
        LocalDateTime now = LocalDateTime.now();
        Booking lastBook = bookRepository.findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(item.getId(), now, now);
        Booking nextBook = bookRepository.findFirstByItemIdAndStartIsAfterOrderByStart(item.getId(), now);
        BookingShort last;
        BookingShort next;
        if (lastBook != null) {
            last = BookingMapper.toBookingShort(lastBook);
            if (lastBook.getStatus() == BookStatus.REJECTED) {
                last = null;
            }
        } else {
            last = null;
        }
        if (nextBook != null) {
            next = BookingMapper.toBookingShort(nextBook);
            if (nextBook.getStatus() == BookStatus.REJECTED) {
                next = null;
            }
        } else {
            next = null;
        }
        if (userId != item.getOwner().getId()) {
            last = null;
            next = null;
        }
        List<CommentResponse> comments = commentRepository.findAllByItem(item).stream()
                .map(CommentMapper::toResponse)
                .collect(Collectors.toList());
        if (comments.isEmpty()) {
            comments = new ArrayList<>();
        }
        return ItemMapper.toItemWithTime(item, last, next, comments);
    }
}
