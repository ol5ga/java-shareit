package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ChangeException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    UserServiceImpl service;
    @Mock
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp(){
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
        List<User> result =  service.getAllUsers();

        assertEquals(1,result.size());
        assertEquals(user,result.get(0));
        assertEquals(user.getId(),result.get(0).getId());
    }

    @Test
    void getUser() {
        when(userRepository.getById(user.getId())).thenReturn(user);
        User result =service.getUser(1);

        assertEquals(user,result);
        assertEquals(user.getEmail(),result.getEmail());
    }

    @Test
    void create() {
        when(userRepository.save(user)).thenReturn(user);
        User result = service.create(user);

        assertEquals(user,result);
        assertEquals(user.getEmail(),result.getEmail());
    }

    @Test
    void updateName() {
        user.setName("New Name");
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.getById(user.getId())).thenReturn(user);
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        users.add(user);
        User result =  service.update(user);

        assertEquals(1,user.getId());
        assertEquals("New Name",result.getName());
    }

    @Test
    void updateEmail(){
        user.setEmail("NewEmail@mail.ru");
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.getById(user.getId())).thenReturn(user);
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        users.add(user);
        User result =  service.update(user);

        assertEquals(1,user.getId());
        assertEquals("NewEmail@mail.ru",result.getEmail());
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
        verify(userRepository,times(1)).deleteById(user.getId());
    }
}