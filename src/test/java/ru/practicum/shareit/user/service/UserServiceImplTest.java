package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl service;
    @Mock
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepository);
        user = User.builder()
                .email("User1@Mail.ru")
                .name("User")
                .build();
        user.setId(1);

    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> result = service.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());
    }

    @Test
    void getUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserDto result = service.getUser(1);

        assertEquals(UserMapper.toUserDto(user), result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void create() {
        when(userRepository.save(user)).thenReturn(user);
        UserDto result = service.create(UserMapper.toUserDto(user));

        assertEquals(UserMapper.toUserDto(user), result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateName() {
        user.setName("New Name");
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.getById(user.getId())).thenReturn(user);
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        users.add(user);
        UserDto result = service.update(user.getId(), UserMapper.toUserDto(user));

        assertEquals(1, user.getId());
        assertEquals("New Name", result.getName());
    }

    @Test
    void updateEmail() {
        user.setEmail("NewEmail@mail.ru");
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.getById(user.getId())).thenReturn(user);
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        users.add(user);
        UserDto result = service.update(user.getId(), UserMapper.toUserDto(user));

        assertEquals(1, user.getId());
        assertEquals("NewEmail@mail.ru", result.getEmail());
    }

//    @Test
//    void updateWrongEmail(){
//        when(userRepository.existsById(user.getId())).thenReturn(true);
//        when(userRepository.getById(user.getId())).thenReturn(user);
//        List<User> users = new ArrayList<>();
//        when(userRepository.findAll()).thenReturn(users);
//        users.add(user);
//
//        assertThrows(StorageException.class,() -> service.update(user));
//    }

    @Test
    void delete() {
        service.delete(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}