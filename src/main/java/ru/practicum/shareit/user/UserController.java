package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private long idGenerate = 0;

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
    public UserDto create(@RequestBody @Valid User user){
        user.setId(++idGenerate);
        user = service.create(user);
        return UserMapper.toUserDto(user);
    }

    @PatchMapping
    public UserDto update(@RequestBody @Valid User user){
        user = service.update(user);
        return UserMapper.toUserDto(user) ;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        service.delete(id);
    }
}