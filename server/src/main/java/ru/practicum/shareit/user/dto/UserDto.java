package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.user.annotation.UserCreate;
import ru.practicum.shareit.user.annotation.UserId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
@Generated
public class UserDto {
    private long id;
    private String name;
    private String email;
}