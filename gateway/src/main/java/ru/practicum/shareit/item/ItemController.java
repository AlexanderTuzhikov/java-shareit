package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @Valid @RequestBody NewItemDto newItemDto) {
        return itemClient.postItem(userId, newItemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable("itemId") Long itemId, @Valid @RequestBody UpdateItemDto updateItemDto) {
        return itemClient.patchItem(userId, itemId, updateItemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId) {
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("itemId") Long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsSearch(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam("text") String text,
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getItemsSearch(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable("itemId") Long itemId,
                                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        return itemClient.postComment(userId, itemId, newCommentDto);
    }
}
