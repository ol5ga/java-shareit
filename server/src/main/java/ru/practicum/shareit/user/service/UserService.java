package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUser(long id);

    UserDto create(UserDto user);

    UserDto update(long id, UserDto user);

    void delete(long id);
}
