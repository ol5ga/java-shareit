package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
//    private final UserMapper mapper;

    @GetMapping
    public List<UserDto> getAllUsers(){
        return service.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id){
        User user = service.getUser(id);
        return UserMapper.toUserDto(user);
    }

    @PostMapping
    public UserDto create(@Validated({UserCreate.class}) @RequestBody UserDto userDto){
        User user = service.create(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable int id,
                           @Valid @RequestBody UserDto userDto){
        User user = service.update(UserMapper.toUser(id, userDto));
        return UserMapper.toUserDto(user) ;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        service.delete(id);
    }
}