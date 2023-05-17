package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class UserStorage {
    public Map<Long, User> users = new HashMap<>();
    long id = 0;
    public List<User> getAllUsers(){
        return new ArrayList<User>(users.values());
    }

    public User getUser(long id){
        if (!users.containsKey(id)) {
            log.info("Неправильный id");
            throw new StorageException("Такого пользователя не существует");
        }
        return users.get(id);
    }

    public User create(User user){
        for (User value : users.values()) {
            if (Objects.equals(user.getEmail(), value.getEmail())){
                log.info("Неправильный email");
                throw new StorageException("Пользователь с таким email существует");
            }
        }
        id++;
        user.setId(id);
        users.put(user.getId(),user);
        return user;
    }

    public User update(User updateUser){
        User oldUser = users.get(updateUser.getId());
        if (!users.containsKey(updateUser.getId())) {
            log.info("Неправильный id");
            throw new StorageException("Такого пользователя не существует");
        } else {

           if(updateUser.getName() == null){
               updateUser.setName(oldUser.getName());
           }
           if(updateUser.getEmail() == null){
               updateUser.setEmail(oldUser.getEmail());
           } else {
               for (User value : users.values()) {
                   if (Objects.equals(updateUser.getEmail(), value.getEmail()) && updateUser.getId()!= value.getId()) {
                       log.info("Неправильный id");
                       throw new StorageException("Пользователь с таким email существует");
                   }
               }
           }
        }
        users.remove(oldUser.getId());
        users.put(updateUser.getId(), updateUser);
        return updateUser;
    }

    public void delete(long id){
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            log.info("Неправильный id");
            throw new StorageException("Такого пользователя не существует");
        }
    }


}
