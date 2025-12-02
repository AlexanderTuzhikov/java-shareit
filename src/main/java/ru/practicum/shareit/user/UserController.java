package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody NewUserDto newUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.postUser(newUserDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> patchUser(@PathVariable("userId") long userId,
                                             @Valid @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok().body(userService.patchUser(userId, updateUserDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") long userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
}
