package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto postUser(NewUserDto newUserDto) {
        log.info("POST user: {}", newUserDto);

        User user = userMapper.mapToUser(newUserDto);
        log.debug("MAP user: {}", user);

        User savedUser = userRepository.save(user);
        log.debug("SAVED user: {}", savedUser);

        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto patchUser(Long userId, UpdateUserDto updateUserDto) {
        log.info("PATCH user: id={}, update={}", userId, updateUserDto);
        User user = checkUserExists(userId);

        User updatedUser = UserMapper.updateUserFields(user, updateUserDto);
        log.debug("UPDATED user: {}", updatedUser);

        User savedUser = userRepository.save(updatedUser);
        log.debug("PATCHED user: {}", savedUser);

        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("DELETE user: id={}", userId);

        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUser(Long userId) {
        log.info("GET user: id={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID=" + userId + " не найден"));
        log.info("FIND user: {}", user);

        return userMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("GET users");

        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::mapToUserDto)
                .toList();
        log.info("FIND users: size={}", users.size());

        return  users;
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User {} not found", userId);
                    return new NotFoundException("User ID=" + userId + " not found");
                });
    }
}

