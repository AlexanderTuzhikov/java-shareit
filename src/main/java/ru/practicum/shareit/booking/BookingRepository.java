package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime actualTimeStart, LocalDateTime actualTimeEnd);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime actualTime);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime startTimeAfter);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus bookingStatus);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime actualTimeStart, LocalDateTime actualTimeEnd);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime actualTime);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime actualTime);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime actualTime, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime actualTime, BookingStatus status);

    Boolean existsByBookerIdAndItem_IdAndEndBefore(Long bookerId, Long itemId, LocalDateTime localDateTime);
}
