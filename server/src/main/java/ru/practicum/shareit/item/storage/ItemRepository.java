package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByRequest(ItemRequest request);

    List<Item> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Item> findAll(Pageable pageable);

    @Query("SELECT i " +
            "FROM Item AS i " +
            "WHERE (lower(i.name) LIKE lower(concat('%', :text,'%')) " +
            "OR lower(i.description) LIKE lower(concat('%', :text,'%'))) " +
            "AND i.available=TRUE")
    public List<Item> search(String text, Pageable pageable);
}
