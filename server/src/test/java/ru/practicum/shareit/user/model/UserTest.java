package ru.practicum.shareit.user.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserTest {
    private final JacksonTester<User> json;

    @Test
    void tesUser() throws Exception {
        User user = new User(1L, "User", "user@mail.ru");

        JsonContent<User> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(user.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }
}