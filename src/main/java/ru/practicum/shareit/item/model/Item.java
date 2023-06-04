package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User owner;

    //TODO ItemRequest request
//    private Long request;
}
