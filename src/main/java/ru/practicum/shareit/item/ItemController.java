package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @Valid @RequestBody NewItemDto newItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.postItem(userId, newItemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("itemId") long itemId, @Valid @RequestBody UpdateItemDto updateItemDto) {
        return ResponseEntity.ok().body(itemService.patchItem(userId, itemId, updateItemDto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable("itemId") long itemId) {
        return ResponseEntity.ok().body(itemService.getItem(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return ResponseEntity.ok().body(itemService.getItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getItemsSearch(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam("text") String text) {
        return ResponseEntity.ok().body(itemService.getItemsSearch(userId, text));
    }
}
