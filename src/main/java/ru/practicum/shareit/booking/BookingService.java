package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto postBooking(Long userId, NewBookingDto newBookingDto);

    BookingDto patchBooking(Long userId, Long bookingId, boolean approved);

    BookingDto getBookings(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long userId, String state);

    List<BookingDto> getOwnerBookings(Long userId, String state);
}
