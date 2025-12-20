package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto postItem(Long userId, NewItemDto newItemDto) {
        log.info("POST item: {}", newItemDto);
        User user = checkUserExists(userId);

        Item item = itemMapper.mapToItem(newItemDto);
        item.setOwner(user);
        log.debug("MAP to item: {}", item);

        Item savedItem = itemRepository.save(item);
        log.debug("SAVED item: {}", savedItem);

        return itemMapper.mapToItemDto(savedItem);
    }

    @Override
    public ItemDto patchItem(Long userId, Long itemId, UpdateItemDto updateItemDto) {
        log.info("PATCH item: id={}, update={}", itemId, updateItemDto);
        checkUserExists(userId);
        Item item = checkItemExists(itemId);
        checkUserAccessToPatch(userId, item);

        Item updatedItem = ItemMapper.updateItemFields(item, updateItemDto);
        log.debug("UPDATED item: {}", updatedItem);

        Item savedItem = itemRepository.save(updatedItem);
        log.debug("PATCHED item: {}", savedItem);

        return itemMapper.mapToItemDto(savedItem);
    }

    @Override
    public void deleteItem(Long itemId) {
        log.info("DELETE item: id={}", itemId);

        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemBookingDto getItems(Long userId, Long itemId) {
        log.info("GET items: id={}", itemId);
        checkUserExists(userId);
        Item item = checkItemExists(itemId);
        log.debug("FIND items: {}", item);

        ItemBookingDto mapItem = mapItemBookingAndComment(item, userId);
        log.debug("MAP items: {}", mapItem);

        return mapItem;
    }

    @Override
    public List<ItemBookingDto> getItems(Long userId) {
        log.info("GET items for user: id={}", userId);
        checkUserExists(userId);

        List<ItemBookingDto> items = itemRepository.findByOwnerId(userId)
                .stream()
                .map(item -> mapItemBookingAndComment(item, userId))
                .toList();
        log.debug("FIND size={} items", items.size());

        return items;
    }

    @Override
    public List<ItemDto> getItemsSearch(Long userId, String text) {
        log.info("GET items search: text={}", text);
        checkUserExists(userId);

        if (text.isBlank()) {
            log.debug("TEXT is blank");
            return List.of();
        }

        List<ItemDto> items = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text)
                .stream()
                .filter(Item::isAvailable)
                .map(itemMapper::mapToItemDto)
                .toList();
        log.debug("FIND size={}", items.size());

        return items;
    }

    @Override
    public CommentDto postComment(Long userId, Long itemId, NewCommentDto newCommentDto) {
        log.info("POST comment: {}", newCommentDto);
        User user = checkUserExists(userId);
        Item item = checkItemExists(itemId);
        checkUserAccessToComment(userId, itemId);

        Comment comment = commentMapper.mapToComment(newCommentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        log.debug("MAP comment: {}", comment);

        Comment savedComment = commentRepository.save(comment);
        log.debug("SAVED comment: {}", comment);

        return commentMapper.mapToCommentDto(savedComment);
    }

    private Item checkItemExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Item {} not found", itemId);
                    return new NotFoundException("Item ID=" + itemId + " not found");
                });
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User {} not found", userId);
                    return new NotFoundException("User ID=" + userId + " not found");
                });
    }

    private void checkUserAccessToPatch(Long userId, Item item) {
        if (!userId.equals(item.getOwner().getId())) {
            log.warn("User ID={} not owner", userId);
            throw new ForbiddenActionException("User ID=" + userId + " not owner item Id=" + item.getId());
        }
    }

    private void checkUserAccessToComment(Long userId, Long itemId) {
        boolean isBooker = bookingRepository.existsByBookerIdAndItem_IdAndEndBefore(userId, itemId, LocalDateTime.now());

        if (!isBooker) {
            log.warn("User ID={} not booker", userId);
            throw new BadRequestException("User ID=" + userId + " not booker item Id=" + itemId);
        }
    }

    private ItemBookingDto mapItemBookingAndComment(Item item, Long userId) {
        ItemBookingDto itemBookingDto = itemMapper.mapToItemBookingDto(item);
        log.debug("MAP to itemBookingDto: {} ", itemBookingDto);

        if (userId.equals(item.getOwner().getId())) {

            itemBookingDto.setLastBooking(bookingRepository.findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(item.getId(),
                            LocalDateTime.now(), BookingStatus.APPROVED)
                    .map(bookingMapper::mapToItemBookingInfoDto)
                    .orElse(null));
            log.debug("SET Last Booking: {} ", itemBookingDto.getLastBooking());

            itemBookingDto.setNextBooking(bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(),
                            LocalDateTime.now(), BookingStatus.APPROVED)
                    .map(bookingMapper::mapToItemBookingInfoDto)
                    .orElse(null));
            log.debug("SET Next Booking: {} ", itemBookingDto.getNextBooking());
        }

        itemBookingDto.setComments(commentRepository.findByItem_IdOrderByCreatedDesc(item.getId())
                .stream()
                .map(commentMapper::mapToCommentDto)
                .toList());
        log.debug("SET Comments: size={} ", itemBookingDto.getComments().size());

        return itemBookingDto;
    }
}

