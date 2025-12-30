package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerIdOrItemOwnerId(Long bookerId, Long ownerId, Pageable pageable);

    Page<Booking> findByBookerIdOrItemOwnerIdAndStartBeforeAndEndAfter(Long bookerId, Long ownerId, LocalDateTime actualTimeStart, LocalDateTime actualTimeEnd, Pageable pageable);

    Page<Booking> findByBookerIdOrItemOwnerIdAndEndBefore(Long bookerId, Long ownerId, LocalDateTime actualTime, Pageable pageable);

    Page<Booking> findByBookerIdOrItemOwnerIdAndStartAfter(Long bookerId, Long ownerId, LocalDateTime startTimeAfter, Pageable pageable);

    Page<Booking> findByBookerIdOrItemOwnerIdAndStatus(Long bookerId, Long ownerId, BookingStatus bookingStatus, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime actualTime, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime actualTime, BookingStatus status);

    Boolean existsByBookerIdAndItem_IdAndEndBefore(Long bookerId, Long itemId, LocalDateTime localDateTime);
}
