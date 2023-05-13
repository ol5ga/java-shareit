package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    UserService service;

    @GetMapping
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id){
        return service.getUser(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user){
      return service.create(user);
    }

    @PatchMapping
    public User update(@RequestBody @Valid User user){
        return service.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        service.delete(id);
    }
}
