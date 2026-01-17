package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @Valid @RequestBody NewtItemRequestDto newtItemRequestDto) {
        return itemRequestClient.postRequest(userId, newtItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable("requestId") Long requestId) {
        return itemRequestClient.getRequest(userId, requestId);
    }
}