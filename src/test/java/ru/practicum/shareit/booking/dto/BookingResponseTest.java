package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingResponseTest {
    @Autowired
    JacksonTester<BookingResponse> tester;
    private User owner;
    private User booker;
    private Item item;
    BookingResponse response;

    @Test
    void testSerialize() throws Exception {
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
        response = BookingResponse.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .item(ItemMapper.toItemDto(item))
                .booker(UserMapper.toUserDto(booker))
                .status(BookStatus.APPROVED)
                .build();

        var result = tester.write(response);
        assertThat(result).hasJsonPath(".$id");
        assertThat(result).hasJsonPath(".$start");
        assertThat(result).hasJsonPath(".$end");
        assertThat(result).hasJsonPath(".$item");
        assertThat(result).hasJsonPath(".$booker");
        assertThat(result).hasJsonPath(".$status");
    }

}