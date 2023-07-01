package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder(toBuilder = true)
@Generated
public class UserDto {
    private long id;
    private String name;
    private String email;
}
