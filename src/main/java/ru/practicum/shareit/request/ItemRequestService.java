package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequest addRequest(long userId, ItemRequestDto request, LocalDateTime created) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> new StorageException("Такого пользователя не существует"));
        ItemRequest IRequest = ItemRequestMapper.toItemRequest(request, requestor, created);
        return repository.save(IRequest);
    }

    public List<ItemRequestResponse> getMyRequests(long userId) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> new StorageException("Такого пользователя не существует"));

        return repository.findAllByRequestorOrderByCreatedDesc(requestor).stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestResponse(itemRequest, findItems(itemRequest)))
                .collect(Collectors.toList());
    }

    public List<ItemRequestResponse> getAllRequests(long userId, Integer from, Integer size) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> new StorageException("Такого пользователя не существует"));
        Pageable page = PageRequest.of(from, size);
        List<ItemRequest> request = repository.findAllByRequestorNotOrderByCreatedDesc(requestor, page);
        return request.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestResponse(itemRequest, findItems(itemRequest)))
                .collect(Collectors.toList());
    }

    public ItemRequestResponse getRequest(long requestId, long userId) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> new StorageException("Такого пользователя не существует"));
        ItemRequest request = repository.findById(requestId).orElseThrow(() -> new ChangeException("Такого запроса не существует"));
        return ItemRequestMapper.toItemRequestResponse(request, findItems(request));
    }

    private List<ItemDto> findItems(ItemRequest request) {
        return itemRepository.findAllByRequest(request).stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }
}
