package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.dto.UserDto.builder;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers() throws Exception {
        UserDto userDto = builder().id(1L).name("name").email("name@mail.ru").build();
        UserDto userDto2 = builder().id(2L).name("name2").email("name2@ya.ru").build();
        List<UserDto> users = List.of(userDto, userDto2);

        when(userService.getAllUsers()).thenReturn(users);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()))
                .andExpect(jsonPath("$[1].id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name").value(userDto2.getName()))
                .andExpect(jsonPath("$[1].email").value(userDto2.getEmail()));
        verify(userService, times(1)).getAllUsers();

    }

    @Test
    void getUser() throws Exception {
        UserDto userDto = builder().id(1L).name("name").email("name@mail.ru").build();

        when(userService.getUser(userDto.getId()))
                .thenReturn(userDto);

        mvc.perform(get("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }


    @Test
    void create() throws Exception {
        UserDto userDtoIn = builder().name("name").email("name@mail.ru").build();
        UserDto userDtoOut = builder().id(1L).name("name").email("name@mail.ru").build();
        when(userService.create(Mockito.any(UserDto.class)))
                .thenReturn(userDtoOut);
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(userDtoOut.getName()))
                .andExpect(jsonPath("$.email").value(userDtoOut.getEmail()));
        verify(userService, times(1)).create(Mockito.any(UserDto.class));
    }

    @Test
    void update() throws Exception {
        UserDto userDtoIn = builder().id(1L).name("name").email("name@mail.ru").build();
        UserDto userDtoOut = builder().id(1L).name("name").email("name@mail.ru").build();
        when(userService.update(anyLong(), Mockito.any(UserDto.class))).thenReturn(userDtoOut);
        mvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id", is(userDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(userDtoOut.getName()))
                .andExpect(jsonPath("$.email").value(userDtoOut.getEmail()));
        verify(userService, times(1)).update(Mockito.anyLong(), Mockito.any(UserDto.class));
    }

}