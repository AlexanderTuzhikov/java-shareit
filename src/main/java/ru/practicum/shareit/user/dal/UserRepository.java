package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User saveUser(User user);

    User updateUser(User user);

    void removeUser(long userId);

    User findUser(long userId);

    List<User> findUsers();

}
