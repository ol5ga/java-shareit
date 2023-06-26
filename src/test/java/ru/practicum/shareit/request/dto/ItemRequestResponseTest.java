package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@JsonTest
class ItemRequestResponseTest {

    @Autowired
    JacksonTester<ItemRequestResponse> tester;

    @Test
    void testSerialize() throws Exception{
        ItemRequestResponse response = ItemRequestResponse.builder()
                .id(1L)
                .description("Item Request")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        var result = tester.write(response);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.items");
    }

}