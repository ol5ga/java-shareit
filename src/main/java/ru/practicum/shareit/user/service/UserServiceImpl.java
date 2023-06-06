package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    @Override
    public List<User> getAllUsers() {
        return storage.findAll();
    }

    @Override
    public User getUser(long id) {

        return storage.getById(id);
    }

    @Override
    public User create(User user) {
        return storage.save(user);
    }

    @Override
    public User update(User user) {
        User oldUser = storage.getById(user.getId());
        checkId(user.getId());
        checkEmail(user,oldUser.getEmail());
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        storage.save(user);
        return user;
    }

    @Override
    public void delete(long id) {
        storage.deleteById(id);
    }

    private void checkId(long userId) {
        try {
            storage.getById(userId);
        } catch (NullPointerException ex){
            log.warn("Неправильный id");
            throw new StorageException("Такого пользователя не существует");
        }
    }

    private void checkEmail(User user, String email){
        for (User value : storage.findAll()) {
            if (Objects.equals(user.getEmail(), value.getEmail()) && user.getId() != value.getId()) {
                log.warn("Неправильный id");
                throw new StorageException("Пользователь с таким email существует");
            }
        }
    }
}