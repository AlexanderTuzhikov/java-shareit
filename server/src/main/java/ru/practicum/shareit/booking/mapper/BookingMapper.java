package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemBookingInfoDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking mapToBooking(NewBookingDto newBookingDto);

    @Mapping(target = "booker", source = "booker", qualifiedByName = "mapToUserDto")
    @Mapping(target = "item", source = "item", qualifiedByName = "mapToItemDto")
    BookingDto mapToBookingDto(Booking booking);

    ItemBookingInfoDto mapToItemBookingInfoDto(Booking booking);

    @Named("mapToUserDto")
    default UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    @Named("mapToItemDto")
    default ItemDto mapToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(), mapToUserDto(item.getOwner()));
    }
}
