package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto postUser(NewUserDto newUserDto) {
        User user = userMapper.mapToUser(newUserDto);
        checkUserEmail(newUserDto.getEmail(), user);
        User savedUser = userRepository.saveUser(user);

        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto patchUser(long userId, UpdateUserDto updateUserDto) {
        User user = checkUserExists(userId);

        if (updateUserDto.hasEmail()) {
            checkUserEmail(updateUserDto.getEmail(), user);
        }

        User updatedUser = UserMapper.updateUserFields(user, updateUserDto);
        User updatedUserFromRepository = userRepository.updateUser(updatedUser);

        return userMapper.mapToUserDto(updatedUserFromRepository);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.removeUser(userId);
    }

    @Override
    public UserDto getUser(long userId) {
        return userMapper.mapToUserDto(userRepository.findUser(userId));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findUsers().stream()
                .map(userMapper::mapToUserDto)
                .toList();
    }

    private User checkUserExists(Long userId) {
        return userRepository.findUser(userId);
    }

    private void checkUserEmail(String email, User user) {
        List<User> users = userRepository.findUsers();

        boolean emailExist = users.stream()
                .anyMatch(userFromMemory -> userFromMemory.getEmail().equals(email)
                        && userFromMemory.getId() != user.getId());

        if (emailExist) {
            throw new ConflictException("Email " + user.getEmail() + " уже используется");
        }
    }
}

