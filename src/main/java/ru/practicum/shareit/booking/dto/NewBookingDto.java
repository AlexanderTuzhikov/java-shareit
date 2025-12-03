package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class NewBookingDto {
    @NotNull(message = "Дата начала бронирования не заполнена")
    LocalDateTime start;
    @NotNull(message = "Дата окончания бронирования не заполнена")
    LocalDateTime end;
    @NotNull(message = "Id вещи для бронирования не заполнен")
    long itemId;
}
