package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class NewtItemRequestDtoTest {
    private final JacksonTester<NewtItemRequestDto> json;

    @Test
    void tesNewtItemRequestDto() throws Exception {
        NewtItemRequestDto newtItemRequestDto = new NewtItemRequestDto("Description", LocalDateTime.now().withNano(0));

        JsonContent<NewtItemRequestDto> result = json.write(newtItemRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(newtItemRequestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(newtItemRequestDto.getCreated().toString());
    }
}