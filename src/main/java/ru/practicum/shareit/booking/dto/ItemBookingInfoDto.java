package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemBookingInfoDto {
    LocalDateTime start;
    LocalDateTime end;
}
