package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> postRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestBody NewtItemRequestDto newtItemRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemRequestService.postRequest(userId, newtItemRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemRequestService.getRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemRequestService.getAllRequests(userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable("requestId") Long requestId) {
        return ResponseEntity.ok().body(itemRequestService.getRequest(userId, requestId));
    }
}