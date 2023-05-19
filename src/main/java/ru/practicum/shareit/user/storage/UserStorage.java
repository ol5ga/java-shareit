package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User getUser(long id);

    User create(User user);

    User update(User updateUser);

    void delete(long id);
}
