package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private long id;
    @NotBlank
    private String name;
    @Email
    private String email;

    public UserDto (String name, String email){
        this.name = name;
        this.email = email;
    }
}
