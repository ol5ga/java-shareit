package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerOrderByStartDesc(User user);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime now, LocalDateTime nowE);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, BookStatus status);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(User user);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime now, LocalDateTime nowE);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(User user, BookStatus status);

    Booking findFirstByItemIdAndStartIsBeforeOrStartEqualsOrderByStartDesc(long id, LocalDateTime now, LocalDateTime nowE);

    Booking findFirstByItemIdAndStartIsAfterOrderByStart(long id, LocalDateTime now);

    Booking findFirstByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(long userId, long itemId, LocalDateTime now);

}