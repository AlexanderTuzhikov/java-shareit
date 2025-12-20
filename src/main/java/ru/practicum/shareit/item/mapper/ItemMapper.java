package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "request", ignore = true)
    Item mapToItem(NewItemDto newItemDto);

    @Mapping(target = "owner", source = "owner", qualifiedByName = "mapToUserDto")
    ItemDto mapToItemDto(Item item);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ItemBookingDto mapToItemBookingDto(Item item);

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

    @Named("mapToUserDto")
    default UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
