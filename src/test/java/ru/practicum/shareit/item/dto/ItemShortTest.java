package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemShortTest {
    @Autowired
    JacksonTester<ItemShort> tester;

    @Test
    void testSerialize() throws Exception {
        ItemShort item = ItemShort.builder()
                .id(1L)
                .name("name")
                .ownerId(1L)
                .build();

        var result = tester.write(item);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.ownerId");

    }


}