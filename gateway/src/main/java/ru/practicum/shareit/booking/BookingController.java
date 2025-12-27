package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exception.ConflictException;

import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody @Valid NewBookingDto newBookingDto) {
        checkBookingStartAndEnd(newBookingDto);
        return bookingClient.postBooking(userId, newBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PathVariable("bookingId") Long bookingId,
                                               @RequestParam(name = "approved") Boolean approved) {
        if (approved == null) {
            return ResponseEntity.badRequest().body("Параметр approved обязателен");
        }
        return bookingClient.patchBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        return bookingClient.getBookings(userId, state, from, size);
    }

    private void checkBookingStartAndEnd(NewBookingDto newBookingDto) {
        LocalDateTime start = newBookingDto.getStart();
        LocalDateTime end = newBookingDto.getEnd();

        if (start.equals(end)) {
            log.warn("Conflict: start={} == end={}", start, end);
            throw new ConflictException("Conflict: start=" + start + " == end=" + end + " ");
        }

        if (start.isAfter(end)) {
            log.warn("Conflict: start={} after end={}", start, end);
            throw new ConflictException("Conflict: start=" + start + " after end=" + end + " ");
        }
    }

}
