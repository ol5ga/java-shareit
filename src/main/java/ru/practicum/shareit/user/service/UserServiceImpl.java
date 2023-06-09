package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUser(long id) {

        return repository.getById(id);
    }

    @Override
    @Transactional
    public User create(User user) {

        return repository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
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
        return user;
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
                log.warn("Неправильный id");
                throw new StorageException("Пользователь с таким email существует");
            }
        }
    }
}