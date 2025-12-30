package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> postUser(@RequestBody NewUserDto newUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.postUser(newUserDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> patchUser(@PathVariable("userId") Long userId,
                                             @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok().body(userService.patchUser(userId, updateUserDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(userService.getUsers(requesterId, from, size));
    }
}
