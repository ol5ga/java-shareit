package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long id) {
        User user = repository.findById(id).orElseThrow(() -> new ChangeException("Такого пользователя не существует"));
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(long id, UserDto userDto) {
        User user = UserMapper.toUser(id, userDto);
        checkId(user.getId());
        User oldUser = repository.getById(user.getId());
        checkEmail(user, oldUser.getEmail());
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        repository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void delete(long id) {

        repository.deleteById(id);
    }

    private void checkId(long userId) {
        if (!repository.existsById(userId)) {
            throw new StorageException("Такого пользователя не существует");
        }
    }

    private void checkEmail(User user, String email) {
        for (User value : repository.findAll()) {
            if (Objects.equals(user.getEmail(), value.getEmail()) && user.getId() != value.getId()) {
                log.warn("Неправильный email");
                throw new StorageException("Пользователь с таким email существует");
            }
        }
    }
}