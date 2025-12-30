package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewCommentDto {
    @NotNull(message = "text is null")
    @NotBlank(message = "text is blank")
    private String text;
    private LocalDateTime created = LocalDateTime.now();
}
