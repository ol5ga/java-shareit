package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("User1@Mail.ru")
                .name("User")
                .build();
        user.setId(1);
    }

    @Test
    void toUserDto() {
        UserDto result = UserMapper.toUserDto(user);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void toUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("User name")
                .email("user@mail.ru")
                .build();

        User result = UserMapper.toUser(userDto);

        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void toUserWithId() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("User name")
                .email("user@mail.ru")
                .build();

        User result = UserMapper.toUser(userDto.getId(), userDto);

        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }
}