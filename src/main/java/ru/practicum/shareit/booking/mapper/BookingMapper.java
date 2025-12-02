package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking mapToBooking(NewBookingDto newBookingDto);

    BookingDto mapToBookingDto(Booking booking);

    static Booking updateBookingFields(Booking booking, UpdateBookingDto updateBookingDto) {
        if (updateBookingDto.hasStart()) {
            booking.setStart(updateBookingDto.getStart());
        }

        if (updateBookingDto.hasEnd()) {
            booking.setEnd(updateBookingDto.getEnd());
        }

        if (updateBookingDto.hasStatus()) {
            booking.setStatus(updateBookingDto.getStatus());
        }

        return booking;
    }
}
