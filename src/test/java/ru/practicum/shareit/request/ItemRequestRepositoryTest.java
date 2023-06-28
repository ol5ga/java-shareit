package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository repository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private ItemRequest request;
    private LocalDateTime now = LocalDateTime.now();

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("requestor@mail.ru")
                .name("requestor")
                .build();
        userRepository.save(user);
        request = ItemRequest.builder()
                .description("text of request")
                .requestor(user)
                .created(now)
                .build();
        repository.save(request);
    }

    @Test
    void findAllByRequestorOrderByCreatedDesc() {
        List<ItemRequest> result = repository.findAllByRequestorOrderByCreatedDesc(user);

        assertEquals(1, result.size());
        assertThat(result.contains(request));
    }

    @Test
    void findAllByRequestorNotOrderByCreatedDesc() {
        User user2 = User.builder()
                .email("requestor2@mail.ru")
                .name("requestor2")
                .build();
        userRepository.save(user2);
        ItemRequest request2 = ItemRequest.builder()
                .description("text of request2")
                .requestor(user2)
                .created(now.minusHours(2))
                .build();
        repository.save(request2);

        List<ItemRequest> result = repository.findAllByRequestorNotOrderByCreatedDesc(user, Pageable.unpaged());

        assertEquals(1, result.size());
        assertThat(result.contains(request2));
        assertNotEquals(request, result.get(0));
    }
}