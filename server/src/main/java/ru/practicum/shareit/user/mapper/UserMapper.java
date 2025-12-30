package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User mapToUser(NewUserDto newUserDto);

    UserDto mapToUserDto(User user);

    static User updateUserFields(User user, UpdateUserDto updateUserDto) {
        if (updateUserDto.hasName()) {
            user.setName(updateUserDto.getName());
        }

        if (updateUserDto.hasEmail()) {
            user.setEmail(updateUserDto.getEmail());
        }

        return user;
    }
}
