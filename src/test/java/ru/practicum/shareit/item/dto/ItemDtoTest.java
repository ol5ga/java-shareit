package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@JsonTest
class ItemDtoTest {

    @Autowired
    JacksonTester<ItemDto> tester;

    @Test
    void testSerialize() throws Exception{

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description item")
                .available(true)
                .requestId(1L)
                .build();

        var result = tester.write(itemDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");
    }

}