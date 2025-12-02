package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class UpdateBookingDto {
    @NotNull(message = "ID бронирования не заполнен")
    long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;

    public boolean hasStart() {
        return !(start == null);
    }

    public boolean hasEnd() {
        return !(end == null);
    }

    public boolean hasStatus() {
        return !(status == null);
    }

}
