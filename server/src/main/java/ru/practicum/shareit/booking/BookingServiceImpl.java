package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto postBooking(Long userId, NewBookingDto newBookingDto) {
        log.info("POST booking: {}", newBookingDto);
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
    @Transactional
    public BookingDto patchBooking(Long userId, Long bookingId, Boolean approved) {
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
    public BookingDto getBooking(Long userId, Long bookingId) {
        log.info("GET bookings: id={}", bookingId);
        Booking booking = checkBookingExists(bookingId);
        checkUserAccessToGetBooking(userId, booking);
        log.debug("FIND bookings: {}", booking);

        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state, Integer from, Integer size) {
        log.info("GET bookings: state={}, from={}, size={}", state, from, size);
        checkUserExists(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        return switch (state) {
            case "ALL" -> bookingRepository.findByBookerIdOrItemOwnerId(userId, userId, pageable)
                    .map(bookingMapper::mapToBookingDto)
                    .stream().toList();
            case "CURRENT" ->
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStartBeforeAndEndAfter(userId, userId, LocalDateTime.now(), LocalDateTime.now(), pageable)
                            .map(bookingMapper::mapToBookingDto)
                            .stream().toList();
            case "PAST" ->
                    bookingRepository.findByBookerIdOrItemOwnerIdAndEndBefore(userId, userId, LocalDateTime.now(), pageable)
                            .map(bookingMapper::mapToBookingDto)
                            .stream().toList();
            case "FUTURE" ->
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStartAfter(userId, userId, LocalDateTime.now(), pageable)
                            .map(bookingMapper::mapToBookingDto)
                            .stream().toList();
            case "WAITING" ->
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStatus(userId, userId, BookingStatus.WAITING, pageable)
                            .map(bookingMapper::mapToBookingDto)
                            .stream().toList();
            case "REJECTED" ->
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStatus(userId, userId, BookingStatus.REJECTED, pageable)
                            .map(bookingMapper::mapToBookingDto)
                            .stream().toList();
            default -> List.of();

        };
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User ID={} not found", userId);
                    return new NotFoundException("User ID=" + userId + " not found");
                });
    }

    private Item checkItemExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item {} not found", itemId);
                    return new NotFoundException("Item ID=" + itemId + " not found");
                });
    }

    private Booking checkBookingExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking {} not found", bookingId);
                    return new NotFoundException("Booking ID=" + bookingId + " not found");
                });
    }

    private void checkItemAvailable(Item item) {
        if (!item.isAvailable()) {
            log.warn("Item Id={} not available", item.getId());
            throw new ConflictException("Item Id=" + item.getId() + " not available");
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
