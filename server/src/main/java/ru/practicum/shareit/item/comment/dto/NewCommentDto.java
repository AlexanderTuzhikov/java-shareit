package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewCommentDto {
    private String text;
    private LocalDateTime created = LocalDateTime.now();
}