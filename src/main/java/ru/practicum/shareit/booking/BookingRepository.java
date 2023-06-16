package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerOrderByStartDesc(User user, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime now, LocalDateTime nowE, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, BookStatus status, Pageable pageable);

    Page<Booking> findAllByItem_OwnerOrderByStartDesc(User user, Pageable pageable);

    Page<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime now, LocalDateTime nowE, Pageable pageable);

    Page<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(User user, BookStatus status, Pageable pageable);

    Booking findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(long id, LocalDateTime now, LocalDateTime nowE);

    Booking findFirstByItemIdAndStartIsAfterOrderByStart(long id, LocalDateTime now);

    Booking findFirstByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(long userId, long itemId, LocalDateTime now);

}
