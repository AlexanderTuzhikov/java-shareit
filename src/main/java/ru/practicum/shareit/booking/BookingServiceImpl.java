package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto postBooking(Long userId, NewBookingDto newBookingDto) {
        log.info("POST booking: {}", newBookingDto);
        checkBookingStartAndEnd(newBookingDto);
        User user = checkUserExists(userId);
        Item item = checkItemExists(newBookingDto.getItemId());
        checkItemAvailable(item);

        Booking booking = bookingMapper.mapToBooking(newBookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        log.debug("MAP to booking: {}", booking);

        Booking savedBooking = bookingRepository.save(booking);
        log.debug("SAVED booking: {}", savedBooking);

        return bookingMapper.mapToBookingDto(savedBooking);
    }

    @Override
    public BookingDto patchBooking(Long userId, Long bookingId, boolean approved) {
        log.info("PATCH booking: id={}, approved={}", bookingId, approved);
        Booking booking = checkBookingExists(bookingId);
        checkUserAccessToPatchBooking(userId, booking);

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking patchedBooking = bookingRepository.save(booking);
        log.debug("PATCHED booking: {}", patchedBooking);

        return bookingMapper.mapToBookingDto(patchedBooking);
    }

    @Override
    public BookingDto getBookings(Long userId, Long bookingId) {
        log.info("GET bookings: id={}", bookingId);
        Booking booking = checkBookingExists(bookingId);
        checkUserAccessToGetBooking(userId, booking);
        log.debug("FIND bookings: {}", booking);

        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state) {
        log.info("GET bookings: state={}", state);
        checkUserExists(userId);

        return switch (state) {
            case "ALL" -> bookingRepository.findByBookerIdOrderByStartDesc(userId)
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            case "CURRENT" ->
                    bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
                            .stream()
                            .map(bookingMapper::mapToBookingDto)
                            .toList();
            case "PAST" -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            case "FUTURE" -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            case "WAITING" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            case "REJECTED" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            default -> List.of();
        };
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long userId, String state) {
        log.info("GET owner bookings: state={}", state);
        checkUserExists(userId);

        return switch (state) {
            case "ALL" -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId)
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            case "CURRENT" ->
                    bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
                            .stream()
                            .map(bookingMapper::mapToBookingDto)
                            .toList();
            case "PAST" -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                    .stream()
                    .map(bookingMapper::mapToBookingDto)
                    .toList();
            case "FUTURE" ->
                    bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                            .stream()
                            .map(bookingMapper::mapToBookingDto)
                            .toList();
            case "WAITING" ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
                            .stream()
                            .map(bookingMapper::mapToBookingDto)
                            .toList();
            case "REJECTED" ->
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
                            .stream()
                            .map(bookingMapper::mapToBookingDto)
                            .toList();
            default -> List.of();
        };
    }

    private Booking checkBookingExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking {} not found", bookingId);
                    return new NotFoundException("Booking ID=" + bookingId + " not found");
                });
    }

    private Item checkItemExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item {} not found", itemId);
                    return new NotFoundException("Item ID=" + itemId + " not found");
                });
    }

    private void checkItemAvailable(Item item) {
        if (!item.isAvailable()) {
            log.warn("Item Id={} not available", item.getId());
            throw new ConflictException("Item Id=" + item.getId() + " not available");
        }
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User {} not found", userId);
                    return new NotFoundException("User ID=" + userId + " not found");
                });
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

    private void checkUserAccessToPatchBooking(Long userId, Booking booking) {
        Long itemOwnerId = booking.getItem()
                .getOwner()
                .getId();

        if (!Objects.equals(userId, itemOwnerId)) {
            log.warn("User ID={} not owner", userId);
            throw new ForbiddenActionException("User ID=" + userId + " not owner");
        }
    }

    private void checkUserAccessToGetBooking(Long userId, Booking booking) {
        Long itemOwnerId = booking.getItem()
                .getOwner()
                .getId();
        Long bookerId = booking.getBooker()
                .getId();

        if (!Objects.equals(userId, itemOwnerId) && !Objects.equals(userId, bookerId)) {
            log.warn("User ID={} not owner or booker", userId);
            throw new ForbiddenActionException("User ID=" + userId + " not owner or booker");
        }
    }
}
