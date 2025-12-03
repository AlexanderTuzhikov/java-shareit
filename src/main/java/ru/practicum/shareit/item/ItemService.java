package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto postItem(long userId, NewItemDto newItem);

    ItemDto patchItem(long userId, long itemId, UpdateItemDto updateItemDto);

    void deleteItem(long itemId);

    ItemDto getItem(long userId, long itemId);

    List<ItemDto> getItems(long userId);

    List<ItemDto> getItemsSearch(long userId, String text);
}
