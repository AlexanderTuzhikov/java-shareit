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
class UpdateUserDtoTest {
    private final JacksonTester<UpdateUserDto> json;

    @Test
    void testUpdateUserDto() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("User", "user@mail.ru");

        JsonContent<UpdateUserDto> result = json.write(updateUserDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(updateUserDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(updateUserDto.getEmail());
    }
}