package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

@Data
public class UpdateCommentDto {
    private String text;

    public boolean hasText() {
        return !(text == null || text.isBlank());
    }
}
