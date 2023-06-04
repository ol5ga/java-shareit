package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
//    List<User> getAllUsers();
//
//    User getUser(long id);
//
//    User create(User user);
//
//    User update(User updateUser);
//
//    void delete(long id);
}
