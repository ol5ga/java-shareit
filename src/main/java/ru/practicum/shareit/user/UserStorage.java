package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.StorageException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class UserStorage {
    public Map<Long,User> users = new HashMap<>();
    long id = 0;
    public List<User> getAllUsers(){
        return new ArrayList<User>(users.values());
    }
    public User getUser(int id){
        if (!users.containsKey(id)) {
            throw new StorageException("Такого пользователя не существует");
        }
        return users.get(id);
    }

    public User create(User user){
        id++;
        user.setId(id);
        users.put(user.getId(),user);
        return user;
    }

    public User update(User updateUser){
        if (users.containsKey(updateUser.getId())) {
            users.put(updateUser.getId(), updateUser);
        } else {
            throw new StorageException("Такого пользователя не существует");
        }
        return updateUser;
    }

    public void delete(int id){
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new StorageException("Такого пользователя не существует");
        }
    }
}
