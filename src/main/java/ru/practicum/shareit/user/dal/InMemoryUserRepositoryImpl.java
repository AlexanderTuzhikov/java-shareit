package ru.practicum.shareit.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User saveUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("User с ID=" + user.getId() + " не найден");
        }

        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    @Override
    public void removeUser(long userId) {
        users.remove(userId);
    }

    @Override
    public User findUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User с ID=" + userId + " не найден");
        }

        return users.get(userId);
    }

    @Override
    public List<User> findUsers() {
        return users.values().stream()
                .toList();
    }
}
