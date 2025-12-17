package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto postUser(NewUserDto newUserDto);

    UserDto patchUser(Long userId, UpdateUserDto updateUserDto);

    void deleteUser(Long userId);

    UserDto getUser(Long userId);

    List<UserDto> getUsers();
}
