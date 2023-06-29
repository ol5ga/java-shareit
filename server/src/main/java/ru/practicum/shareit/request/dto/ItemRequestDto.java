package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
@Generated
public class ItemRequestDto {
    @NotBlank
    private String description;
    @NotNull
    private long requestor;
}
