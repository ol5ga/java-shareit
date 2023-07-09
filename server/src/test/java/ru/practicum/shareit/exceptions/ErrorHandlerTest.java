package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void testHandleValidationException() {
        ValidationException exception = new ValidationException("error");
        ErrorResponse errorResponse = errorHandler.handleValidationException(exception);
        assertNotNull(errorResponse);
        assertEquals(exception.getMessage(), errorResponse.getError());
    }

    @Test
    void handleChangeException() {
        ChangeException exception = new ChangeException("error");
        ErrorResponse errorResponse = errorHandler.handleChangeException(exception);
        assertNotNull(errorResponse);
        assertEquals(exception.getMessage(), errorResponse.getError());
    }

    @Test
    void testHandleChangeException() {
        ChangeException exception = new ChangeException("error");
        ErrorResponse errorResponse = errorHandler.handleChangeException(exception);
        assertNotNull(errorResponse);
        assertEquals(exception.getMessage(), errorResponse.getError());
    }
}