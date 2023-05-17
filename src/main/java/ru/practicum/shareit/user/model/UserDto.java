package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.user.UserCreate;
import ru.practicum.shareit.user.UserId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
public class UserDto {
    @Null(groups = UserId.class)
    private long id;
    @NotNull(groups = UserCreate.class)
    @Nullable
    private String name;
    @Email(message = "Email not valid", groups = UserCreate.class)
    @NotNull(groups = UserCreate.class)
    @Nullable
    private String email;


}
