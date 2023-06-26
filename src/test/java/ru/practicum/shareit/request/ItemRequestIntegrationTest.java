package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestIntegrationTest {
    @Autowired
    private ItemRequestService service;
    @Autowired
    private ItemRequestRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private Item item;
    private User owner;
    private User booker;
    private LocalDateTime now = LocalDateTime.now();


    @BeforeEach
    void setUp() {
        service = new ItemRequestService(repository, userRepository, itemRepository);
        owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        userRepository.save(owner);
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);
        booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        userRepository.save(booker);
    }

    @Test
    void addRequest() {
        ItemRequestDto request = new ItemRequestDto("Request of item",2);

        ItemRequest result = service.addRequest(booker.getId(),request, now);

        assertEquals(1,result.getId());
        assertEquals(request.getDescription(),request.getDescription());
        assertEquals(request.getRequestor(),result.getRequestor().getId());
        assertEquals(booker,result.getRequestor());
    }

    @Test
    void getRequest() {
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request")
                .requestor(booker)
                .created(now)
                .build();
        repository.save(request);
        ItemRequestResponse result = service.getRequest(request.getId(), booker.getId());

        assertEquals(1,result.getId());
        assertEquals(request.getDescription(),result.getDescription());
    }
}