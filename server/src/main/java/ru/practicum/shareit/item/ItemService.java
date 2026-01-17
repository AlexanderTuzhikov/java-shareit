package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

public interface ItemService {
    ItemDto postItem(Long userId, NewItemDto newItem);

    ItemDto patchItem(Long userId, Long itemId, UpdateItemDto updateItemDto);

    void deleteItem(Long userId,Long itemId);

    ItemBookingDto getItem(Long userId, Long itemId);

    Page<ItemBookingDto> getItems(Long userId, Integer from, Integer size);

    Page<ItemDto> getItemsSearch(Long userId, String text, Integer from, Integer size);

    CommentDto postComment(Long userId, Long itemId, NewCommentDto newCommentDto);
}
