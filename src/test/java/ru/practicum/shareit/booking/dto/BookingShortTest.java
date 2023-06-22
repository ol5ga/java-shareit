package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@JsonTest
class BookingShortTest {
    @Autowired
    JacksonTester<BookingShort> tester;


    @Test
    void testSerialize() throws Exception {
        User booker = User.builder()
                .id(1)
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();

        BookingShort booking = BookingShort.builder()
                .id(1L)
                .bookerId(booker.getId())
                .build();

        var result = tester.write(booking);
        assertThat(result).hasJsonPath(".$id");
        assertThat(result).hasJsonPath(".$bookerId");

    }
}