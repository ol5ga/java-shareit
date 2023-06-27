package ru.practicum.shareit.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageExceptionTest {
    @Test
    void testConstructor() {
        StorageException storageException = new StorageException("Не найдено");
        assertNull(storageException.getCause());
        assertEquals(0, storageException.getSuppressed().length);
        assertEquals("Не найдено", storageException.getMessage());
        assertEquals("Не найдено", storageException.getLocalizedMessage());
    }
}