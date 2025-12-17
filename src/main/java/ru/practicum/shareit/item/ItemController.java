package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @Valid @RequestBody NewItemDto newItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.postItem(userId, newItemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId, @Valid @RequestBody UpdateItemDto updateItemDto) {
        return ResponseEntity.ok().body(itemService.patchItem(userId, itemId, updateItemDto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemBookingDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok().body(itemService.getItems(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemBookingDto>> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemService.getItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getItemsSearch(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam("text") String text) {
        return ResponseEntity.ok().body(itemService.getItemsSearch(userId, text));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable("itemId") Long itemId,
                                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.postComment(userId, itemId, newCommentDto));
    }
}
