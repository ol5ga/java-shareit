package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    public ItemRequest addRequest(long userId, ItemRequestDto request, LocalDateTime created){
        checkUser(userId);
        User requestor = userRepository.findById(userId).orElseThrow();
        ItemRequest IRequest = ItemRequestMapper.toItemRequest(request, requestor,created);
        return repository.save(IRequest);
    }

    public List<ItemRequest> getMyRequests(long userId){
        return null;
    }

    public List<ItemRequest> getAllRequests(){
        return null;
    }

    public ItemRequest getRequest(){
        return null;
    }

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new StorageException("Такого пользователя не существует");
        }
    }
}
