package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.ItemBookingInfoDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.List;

@Data
public class ItemBookingDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private ItemBookingInfoDto lastBooking;
    private ItemBookingInfoDto nextBooking;
    private List<CommentDto> comments;
}
