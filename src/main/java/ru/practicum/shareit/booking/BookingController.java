package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
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
                                                  @Valid @RequestBody NewBookingDto newBookingDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.postBooking(userId, newBookingDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> patchBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable("bookingId") Long bookingId,
                                                   @RequestParam(name = "approved") boolean approved) {
        return ResponseEntity.ok().body(bookingService.patchBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable("bookingId") Long bookingId) {
        return ResponseEntity.ok().body(bookingService.getBookings(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return ResponseEntity.ok().body(bookingService.getBookings(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return ResponseEntity.ok().body(bookingService.getOwnerBookings(userId, state));
    }


}
