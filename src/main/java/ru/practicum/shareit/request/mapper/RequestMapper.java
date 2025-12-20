package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;
import ru.practicum.shareit.request.dto.UpdateItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest mapToItemRequest(NewtItemRequestDto newtItemRequestDto);

    @Mapping(target = "requestor", source = "requestor", qualifiedByName = "mapToUserDto")
    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest);

    static ItemRequest updateItemRequestFields(ItemRequest itemRequest, UpdateItemRequestDto updateItemRequestDto) {
        if (updateItemRequestDto.hasDescription()) {
            itemRequest.setDescription(updateItemRequestDto.getDescription());
        }

        return itemRequest;
    }

    @Named("mapToUserDto")
    default UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
