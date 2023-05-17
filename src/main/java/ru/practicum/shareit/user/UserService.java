package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;
    public List<User> getAllUsers(){
        return storage.getAllUsers();
    }
    public User getUser(long id){
        return storage.getUser(id);
    }

    public User create(User user){
        return storage.create(user);
    }

    public User update(User user){
        return storage.update(user);
    }

    public void delete(long id){
        storage.delete(id);
    }
}
