package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewtItemRequestDto {
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
