package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.StorageException;

import java.util.*;

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
        for (User value : users.values()) {
            if (Objects.equals(user.getEmail(), value.getEmail())){
                throw new StorageException("Пользователь с таким email существует");
            }
        }
        users.put(user.getId(),user);
        return user;
    }

    public User update(User updateUser){
        if (!users.containsKey(updateUser.getId())) {
            throw new StorageException("Такого пользователя не существует");
        } else {
            User oldUser = users.get(updateUser.getId());
           if(updateUser.getName() == null){
               updateUser.setName(oldUser.getName());
           }
           if(updateUser.getEmail() == null){
               updateUser.setEmail(oldUser.getEmail());
           }
        }
        users.put(updateUser.getId(), updateUser);
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
