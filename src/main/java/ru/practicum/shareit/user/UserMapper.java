package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.model.Item;

@AllArgsConstructor
public class UserMapper {


    public static UserDto toUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto){
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
