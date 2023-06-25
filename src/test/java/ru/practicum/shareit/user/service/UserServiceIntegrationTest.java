package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {
    @Autowired
    private UserService service;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .email("User1@Mail.ru")
                .name("User")
                .build();

    }
    @Test
    void getAllUsers() {
        userRepository.save(user);
        List<User> result = service.getAllUsers();

        assertEquals(1,result.size());
        assertEquals(user,result.get(0));
        assertEquals(user.getId(),result.get(0).getId());
    }

    @Test
    void getUser() {
        userRepository.save(user);
        User result = service.getUser(user.getId());

        assertEquals(user,result);
        assertEquals(user.getEmail(),result.getEmail());
    }

    @Test
    void create() {
        assertEquals(0,user.getId());
        User result = service.create(user);

        assertEquals(1,user.getId());
        assertEquals(user.getEmail(),result.getEmail());
    }

    @Test
    void updateName() {
        userRepository.save(user);
        user.setName("New Name");
        User result = service.update(user);

        assertEquals("New Name",result.getName());
        assertEquals(user.getEmail(),result.getEmail());
    }

    @Test
    void updateEmail() {
        userRepository.save(user);
        user.setEmail("new@mail.ru");
        User result = service.update(user);

        assertEquals(user.getName(),result.getName());
        assertEquals("new@mail.ru",result.getEmail());
    }

    @Test
    void delete() {
        userRepository.save(user);
        assertEquals(1,userRepository.findAll().size());
        service.delete(user.getId());

        assertEquals(0,userRepository.findAll().size());
    }

}