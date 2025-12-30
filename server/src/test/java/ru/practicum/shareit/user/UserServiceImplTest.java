package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    private NewUserDto newUserDto;
    private UpdateUserDto updateUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(null, "Name", "name@mail.ru");
        entityManager.persist(user);
        entityManager.flush();

        newUserDto = new NewUserDto("User", "user@mail.ru");
        updateUserDto = new UpdateUserDto("Update", "update@mail.ru");
    }

    @Test
    @DisplayName("POST User")
    void postUser() {
        UserDto userDto = userService.postUser(newUserDto);
        TypedQuery<User> query = entityManager.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", userDto.getId())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(newUserDto.getName()));
        assertThat(user.getEmail(), equalTo(newUserDto.getEmail()));
    }

    @Test
    @DisplayName("PATCH User")
    void patchUser() {
        userService.patchUser(user.getId(), updateUserDto);
        TypedQuery<User> query = entityManager.createQuery("Select u from User u where u.id = :id", User.class);
        User userFromDb = query.setParameter("id", user.getId())
                .getSingleResult();

        assertThat(userFromDb.getId(), equalTo(user.getId()));
        assertThat(userFromDb.getName(), equalTo(updateUserDto.getName()));
        assertThat(userFromDb.getEmail(), equalTo(updateUserDto.getEmail()));
    }

    @Test
    @DisplayName("DELETE User")
    void deleteUser() {
        UserDto userDto = userService.postUser(newUserDto);
        userService.deleteUser(userDto.getId());

        assertThrows(NotFoundException.class, () -> userService.getUser(userDto.getId()));
    }

    @Test
    @DisplayName("GET User")
    void getUser() {
        UserDto userDto = userService.postUser(newUserDto);
        UserDto userDtoFromDb = userService.getUser(userDto.getId());

        assertThat(userDto, equalTo(userDtoFromDb));
    }

    @Test
    @DisplayName("POST Users")
    void getUsers() {
        UserDto userDto = userService.postUser(newUserDto);
        Page<UserDto> usersDtoFromDb = userService.getUsers(userDto.getId(), 0, 10);

        assertThat(usersDtoFromDb, notNullValue());
        assertThat(usersDtoFromDb.getContent(), not(empty()));
        assertThat(usersDtoFromDb.getSize(), equalTo(10));
    }
}