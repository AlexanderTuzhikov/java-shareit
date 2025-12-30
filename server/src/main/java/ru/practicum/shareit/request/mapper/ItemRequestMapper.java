package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.item.dto.ItemDtoToRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "items", ignore = true)
    ItemRequest mapToItemRequest(NewtItemRequestDto newtItemRequestDto);

    @Mapping(target = "items", source = "items", qualifiedByName = "mapToItemDtoToRequest")
    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest);


    @Named("mapToItemDtoToRequest")
    default List<ItemDtoToRequest> mapToItemDtoToRequest(List<Item> items) {
        if (items != null) {
            return items.stream()
                    .map(item -> new ItemDtoToRequest(item.getId(), item.getName(), item.getOwner().getId()))
                    .toList();
        }

        return new ArrayList<>();
    }
}


