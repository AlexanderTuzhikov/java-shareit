package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto postItem(long userId, NewItemDto newItem) {
        User user = checkUserExists(userId);
        Item item = itemMapper.mapToItem(newItem);
        item.setOwner(user);
        Item savedItem = itemRepository.saveItem(item);

        return itemMapper.mapToItemDto(savedItem);
    }

    @Override
    public ItemDto patchItem(long userId, long itemId, UpdateItemDto updateItemDto) {
        checkUserExists(userId);
        Item item = checkItemExists(itemId);
        checkUserAccess(userId, item);
        Item updatedItem = ItemMapper.updateItemFields(item, updateItemDto);

        return itemMapper.mapToItemDto(itemRepository.updateItem(updatedItem));
    }

    @Override
    public void deleteItem(long itemId) {
        itemRepository.removeItem(itemId);
    }

    @Override
    public ItemDto getItem(long userId, long itemId) {
        checkUserExists(userId);
        Item item = checkItemExists(itemId);
        checkUserAccess(userId, item);

        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        checkUserExists(userId);

        return itemRepository.findItems(userId).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> getItemsSearch(long userId, String text) {
        checkUserExists(userId);

        if (text.isBlank()) {
            return List.of();
        }

        String[] querySearch = Arrays.stream(text.toLowerCase().split(","))
                .map(String::trim)
                .filter(blankString -> !blankString.isBlank())
                .toArray(String[]::new);

        if (querySearch.length == 0) {
            return List.of();
        }

        return itemRepository.findItemsSearch(querySearch).stream()
                .filter(Item::isAvailable)
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    private Item checkItemExists(Long itemId) {
        return itemRepository.findItem(itemId);
    }

    private User checkUserExists(Long userId) {
        return userRepository.findUser(userId);
    }

    private void checkUserAccess(Long userId, Item item) {
        if (userId != item.getOwner().getId()) {
            throw new ForbiddenActionException("User ID=" + userId + " не является владельцем Item Id=" + item.getId());
        }
    }
}

