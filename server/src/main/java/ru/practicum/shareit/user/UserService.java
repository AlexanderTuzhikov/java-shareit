package ru.practicum.shareit.user;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto postUser(NewUserDto newUserDto);

    UserDto patchUser(Long userId, UpdateUserDto updateUserDto);

    void deleteUser(Long userId);

    UserDto getUser(Long userId);

    Page<UserDto> getUsers(Long userId, Integer from, Integer size);
}
