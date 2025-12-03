package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "request", ignore = true)
    Item mapToItem(NewItemDto newItemDto);

    ItemDto mapToItemDto(Item item);

    static Item updateItemFields(Item item, UpdateItemDto updateItemDto) {
        if (updateItemDto.hasName()) {
            item.setName(updateItemDto.getName());
        }

        if (updateItemDto.hasDescription()) {
            item.setDescription(updateItemDto.getDescription());
        }

        if (updateItemDto.hasAvailable()) {
            item.setAvailable(updateItemDto.getAvailable());
        }

        return item;
    }
}
