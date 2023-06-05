package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStorage extends JpaRepository<Booking,Long> {
}
