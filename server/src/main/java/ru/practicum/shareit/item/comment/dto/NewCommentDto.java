package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewCommentDto {
    private String text;
    private LocalDateTime created = LocalDateTime.now();
}