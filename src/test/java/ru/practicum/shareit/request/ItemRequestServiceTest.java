package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    private ItemRequestService service;
    @Mock
    private ItemRequestRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    private Item item;
    private User owner;
    private User booker;

    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp(){
        service = new ItemRequestService(repository,userRepository,itemRepository);
        owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        owner.setId(1);
        item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        item.setId(1);
        booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        booker.setId(2);
    }
    @Test
    void addRequest() {
        ItemRequestDto request = new ItemRequestDto("Request of item",2);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        ItemRequest IRequest = ItemRequestMapper.toItemRequest(request, booker,now);
        IRequest.setId(1);
        when(repository.save(any(ItemRequest.class))).thenReturn(IRequest);

        ItemRequest result = service.addRequest(booker.getId(),request, now);

        assertEquals(1,result.getId());
        assertEquals(request.getDescription(),request.getDescription());
        assertEquals(request.getRequestor(),result.getRequestor().getId());
        assertEquals(booker,result.getRequestor());
    }

    @Test
    void getMyRequests() {
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request")
                .requestor(booker)
                .created(now)
                .build();
        ItemRequestResponse response = ItemRequestMapper.toItemRequestResponse(request, new ArrayList<>());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(repository.findAllByRequestorOrderByCreatedDesc(booker)).thenReturn(List.of(request));

        List<ItemRequestResponse> result = service.getMyRequests(booker.getId());

        assertEquals(1,result.size());
        assertThat(result.contains(response));
    }

    @Test
    void getAllRequests() {
            ItemRequest request = ItemRequest.builder()
                    .id(1)
                    .description("request")
                    .requestor(booker)
                    .created(now)
                    .build();
            ItemRequestResponse response = ItemRequestMapper.toItemRequestResponse(request, new ArrayList<>());
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
            when(repository.findAllByRequestorNotOrderByCreatedDesc(booker, PageRequest.of(1,1))).thenReturn(List.of(request));

            List<ItemRequestResponse> result = service.getAllRequests(booker.getId(),1,1);

            assertEquals(1,result.size());
            assertThat(result.contains(response));
    }

    @Test
    void getRequest() {
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request")
                .requestor(booker)
                .created(now)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(repository.findById(anyLong())).thenReturn(Optional.of(request));

        ItemRequestResponse result = service.getRequest(request.getId(), booker.getId());

        assertEquals(request.getId(),result.getId());
        assertEquals(request.getDescription(),result.getDescription());
    }

}