package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository requestRepository;
    private Item item;
    private ItemRequest request;
    private Item item2;
    private User owner;
    private User requestor;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        requestor = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        userRepository.save(owner);
        userRepository.save(requestor);
        request = ItemRequest.builder()
                .id(1L)
                .description("itemRequest description")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(request);
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .request(request)
                .build();
        item2 = Item.builder()
                .name("name2")
                .description("item2")
                .available(true)
                .owner(owner)
                .request(request)
                .build();
        itemRepository.save(item);
        itemRepository.save(item2);
    }
    @Test
    void testGettingAllItemWithRequest() {
        List<Item> result = itemRepository.findAllByRequest(request);

        assertEquals(2, result.size());
        assertThat(result.contains(item));
        assertEquals(item, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    void testGettingOwnersItems() {
        List<Item> result = itemRepository.findByOwnerId(owner.getId(), Pageable.unpaged());

        assertEquals(2, result.size());
        assertThat(result.contains(item));
        assertEquals(item, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    void testGettingAllItemsWithLimit() {
        Page<Item> page = itemRepository.findAll(Pageable.unpaged());
        List<Item> result = page.stream()
                .collect(Collectors.toList());

        assertEquals(2, result.size());
        assertThat(result.contains(item));
        assertEquals(item, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    void testSearchingInName(){
        List<Item> result = itemRepository.search("name", Pageable.unpaged());

        assertEquals(2, result.size());
        assertThat(result.contains(item));
        assertEquals(item, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    void testSearchingInDescription(){
        List<Item> result = itemRepository.search("item", Pageable.unpaged());

        assertEquals(2, result.size());
        assertThat(result.contains(item));
        assertEquals(item, result.get(0));
        assertEquals(item2, result.get(1));
    }
}