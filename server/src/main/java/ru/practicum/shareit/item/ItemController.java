package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody NewItemDto newItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.postItem(userId, newItemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId, @RequestBody UpdateItemDto updateItemDto) {
        return ResponseEntity.ok().body(itemService.patchItem(userId, itemId, updateItemDto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable("itemId") Long itemId) {
        itemService.deleteItem(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemBookingDto> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok().body(itemService.getItem(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<Page<ItemBookingDto>> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(itemService.getItems(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemDto>> getItemsSearch(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam("text") String text,
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(itemService.getItemsSearch(userId, text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable("itemId") Long itemId,
                                                  @RequestBody NewCommentDto newCommentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.postComment(userId, itemId, newCommentDto));
    }
}
