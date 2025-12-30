package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class NewUserDtoTest {
    private final JacksonTester<NewUserDto> json;

    @Test
    void testNewUserDto() throws Exception {
        NewUserDto newUserDto = new NewUserDto("User", "user@mail.ru");

        JsonContent<NewUserDto> result = json.write(newUserDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(newUserDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(newUserDto.getEmail());
    }
}