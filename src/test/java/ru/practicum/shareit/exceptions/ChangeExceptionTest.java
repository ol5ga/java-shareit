package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangeExceptionTest {
    @Test
    void testConstructor() {
        ChangeException changeException = new ChangeException("Не найдено");
        assertNull(changeException.getCause());
        assertEquals(0, changeException.getSuppressed().length);
        assertEquals("Не найдено", changeException.getMessage());
        assertEquals("Не найдено", changeException.getLocalizedMessage());
    }
}
