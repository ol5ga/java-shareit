package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    JacksonTester<ItemRequestDto> tester;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto request = ItemRequestDto.builder()
                .description("Item request")
                .requestor(1L)
                .build();
        var result = tester.write(request);

        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.requestor");
    }

}