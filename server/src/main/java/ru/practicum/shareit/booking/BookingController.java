package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> postBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestBody NewBookingDto newBookingDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.postBooking(userId, newBookingDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> patchBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable("bookingId") Long bookingId,
                                                   @RequestParam(name = "approved") Boolean approved) {
        return ResponseEntity.ok().body(bookingService.patchBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable("bookingId") Long bookingId) {
        return ResponseEntity.ok().body(bookingService.getBooking(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "state", defaultValue = "all") String state,
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(bookingService.getBookings(userId, state, from, size));
    }
}
