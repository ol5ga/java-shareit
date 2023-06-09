package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Generated;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Generated
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User owner;
    @ManyToOne(targetEntity = ItemRequest.class)
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
