package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto postUser(NewUserDto newUserDto);

    UserDto patchUser(long userId, UpdateUserDto updateUserDto);

    void deleteUser(long userId);

    UserDto getUser(long userId);

    List<UserDto> getUsers();

}
