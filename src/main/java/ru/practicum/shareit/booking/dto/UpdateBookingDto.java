package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class UpdateBookingDto {
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
