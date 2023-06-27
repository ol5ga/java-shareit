package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void testFindAllByOwner() {
    }

    @Test
    void findAllByRequest() {
    }

    @Test
    void findByOwnerId() {
    }

    @Test
    void findAll() {
    }
}