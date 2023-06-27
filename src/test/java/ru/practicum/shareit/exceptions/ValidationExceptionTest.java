package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {
    @Test
    void testConstructor() {
        ValidationException actualValidationException = new ValidationException("Не найдено");
        assertNull(actualValidationException.getCause());
        assertEquals(0, actualValidationException.getSuppressed().length);
        assertEquals("Не найдено", actualValidationException.getMessage());
        assertEquals("Не найдено", actualValidationException.getLocalizedMessage());
    }
}