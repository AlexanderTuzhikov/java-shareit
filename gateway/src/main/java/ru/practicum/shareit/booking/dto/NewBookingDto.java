package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewBookingDto {
    @NotNull(message = "start is null")
    LocalDateTime start;
    @NotNull(message = "end is null")
    LocalDateTime end;
    @NotNull(message = "itemId is null")
    Long itemId;
}
