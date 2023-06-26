package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemWithPropertyTest {
    @Autowired
    JacksonTester<ItemWithProperty> tester;

    @Test
    void testSerialize() throws Exception{
        BookingShort lastBooking = BookingShort.builder()
                .id(1L)
                .bookerId(1L)
                .build();
        BookingShort nextBooking = BookingShort.builder()
                .id(2L)
                .bookerId(1L)
                .build();
        ItemWithProperty item = ItemWithProperty.builder()
                .id(1L)
                .name("name")
                .description("description item")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();
        var result = tester.write(item);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");



    }


}