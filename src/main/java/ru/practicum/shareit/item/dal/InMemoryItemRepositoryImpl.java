package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item saveItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);

        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Item с ID=" + item.getId() + " не найден");
        }

        items.put(item.getId(), item);

        return items.get(item.getId());
    }

    @Override
    public void removeItem(long itemId) {
        items.remove(itemId);
    }

    @Override
    public Item findItem(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item с ID=" + itemId + " не найден");
        }

        return items.get(itemId);

    }

    @Override
    public List<Item> findItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
    }

    @Override
    public List<Item> findItemsSearch(String[] querySearch) {
        List<Item> result = new ArrayList<>();

        for (Item item : items.values()) {
            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();

            for (String query : querySearch) {

                if (name.contains(query) || description.contains(query)) {
                    result.add(item);

                }
            }
        }

        return result;
    }
}
