package ru.practicum.shareit.exceptions;

import ru.practicum.shareit.Generated;

@Generated
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
