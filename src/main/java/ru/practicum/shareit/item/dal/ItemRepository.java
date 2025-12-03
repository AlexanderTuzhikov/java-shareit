package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item saveItem(Item item);

    Item updateItem(Item item);

    void removeItem(long itemId);

    Item findItem(long itemId);

    List<Item> findItems(long userId);

    List<Item> findItemsSearch(String[] querySearch);
}
