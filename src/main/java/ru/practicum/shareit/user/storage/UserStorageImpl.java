package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public List<User> getAllUsers() {

        return new ArrayList<User>(users.values());
    }

    @Override
    public User getUser(long id) {
        checkId(id);
        return users.get(id);
    }

    @Override
    public User create(User user) {
        for (User value : users.values()) {
            if (Objects.equals(user.getEmail(), value.getEmail())) {
                log.warn("Неправильный email");
                throw new StorageException("Пользователь с таким email существует");
            }
        }
        id++;
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User updateUser) {
        User oldUser = users.get(updateUser.getId());
        checkId(id);
        if (updateUser.getName() == null) {
            updateUser.setName(oldUser.getName());
        }
        if (updateUser.getEmail() == null) {
            updateUser.setEmail(oldUser.getEmail());
        } else {
            for (User value : users.values()) {
                if (Objects.equals(updateUser.getEmail(), value.getEmail()) && updateUser.getId() != value.getId()) {
                    log.warn("Неправильный id");
                    throw new StorageException("Пользователь с таким email существует");
                }
            }
        }

        users.remove(oldUser.getId());
        users.put(updateUser.getId(), updateUser);
        return updateUser;
    }

    @Override
    public void delete(long id) {
        checkId(id);
        users.remove(id);

    }

    private void checkId(long userId) {
        if (!users.containsKey(userId)) {
            log.warn("Неправильный id");
            throw new StorageException("Такого пользователя не существует");
        }
    }


}
