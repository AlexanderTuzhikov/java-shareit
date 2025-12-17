package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCommentDto {
    @NotBlank(message = "text is blank")
    private String text;

    public boolean hasText() {
        return !(text == null || text.isBlank());
    }
}
