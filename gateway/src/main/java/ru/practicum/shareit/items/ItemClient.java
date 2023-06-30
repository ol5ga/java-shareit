package ru.practicum.shareit.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.CommentRequest;
import ru.practicum.shareit.items.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(long userId, ItemDto itemDto) {

        return post("", userId, itemDto);
    }


    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemDto itemUserDto) {
        return patch("/" + itemId, userId, itemUserDto);
    }


    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }


    public ResponseEntity<Object> getUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findItem(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );

        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> postComment(long userId, long itemId, CommentRequest commentRequestDto) {
        return post("/" + itemId + "/comment", userId, commentRequestDto);
    }
}