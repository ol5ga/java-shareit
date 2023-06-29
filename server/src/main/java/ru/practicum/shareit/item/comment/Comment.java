package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Generated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Generated
@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne
    @JoinColumn(name = "author_id")
    private User user;

    @Column
    private LocalDateTime created;
}