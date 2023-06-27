package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentResponseTest {
    @Autowired
    JacksonTester<CommentResponse> tester;

    @Test
    void testSerialize() throws Exception {
        CommentResponse response = CommentResponse.builder()
                .id(1L)
                .text("Comment text")
                .authorName("booker")
                .created(LocalDateTime.now())
                .build();
        var result = tester.write(response);
        assertThat(result).hasJsonPath(".$id");
        assertThat(result).hasJsonPath(".$text");
        assertThat(result).hasJsonPath(".authorName");
        assertThat(result).hasJsonPath(".created");
    }
}