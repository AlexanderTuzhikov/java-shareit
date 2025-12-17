package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto postItem(Long userId, NewItemDto newItem);

    ItemDto patchItem(Long userId, Long itemId, UpdateItemDto updateItemDto);

    void deleteItem(Long itemId);

    ItemBookingDto getItems(Long userId, Long itemId);

    List<ItemBookingDto> getItems(Long userId);

    List<ItemDto> getItemsSearch(Long userId, String text);

    CommentDto postComment(Long userId, Long itemId, NewCommentDto newCommentDto);
}
