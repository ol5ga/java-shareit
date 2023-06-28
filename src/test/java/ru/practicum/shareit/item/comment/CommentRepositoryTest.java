package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private LocalDateTime now = LocalDateTime.now();

    @Test
    void findAllByItem() {
        User owner = User.builder()
                .email("ownerItem1@Mail.ru")
                .name("ownerItem1")
                .build();
        userRepository.save(owner);
        Item item = Item.builder()
                .name("name")
                .description("item1")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);
        User booker = User.builder()
                .email("bookerItem1@mail.ru")
                .name("booker")
                .build();
        User booker2 = User.builder()
                .email("booker2@mail.ru")
                .name("booker2")
                .build();
        userRepository.save(booker);
        userRepository.save(booker2);
        Comment comment1 = CommentMapper.toComment(new CommentRequest("Comment about item"), item, booker, now);
        Comment comment2 = CommentMapper.toComment(new CommentRequest("Comment about item"), item, booker2, now.minusHours(2));
        repository.save(comment1);
        repository.save(comment2);

        List<Comment> result = repository.findAllByItem(item);

        assertEquals(2, result.size());
        assertThat(result.contains(comment1));
        assertThat(result.contains(comment2));
    }
}