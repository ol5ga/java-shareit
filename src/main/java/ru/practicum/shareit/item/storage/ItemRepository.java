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

    @Query("SELECT i from Item i WHERE upper(i.available) LIKE upper('true') AND (upper(i.name) LIKE upper(CONCAT('%', ?1, '%')) " +
            "OR upper(i.description) LIKE upper(CONCAT('%', ?1, '%')))")
    public List<Item> search(String text, Pageable pageable);
}
