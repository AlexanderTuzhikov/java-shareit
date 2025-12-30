package ru.practicum.shareit.item.comment.dto;

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
class NewCommentDtoTest {
    private final JacksonTester<NewCommentDto> json;

    @Test
    void tesNewCommentDto() throws Exception {
        NewCommentDto newCommentDto = new NewCommentDto("Text", LocalDateTime.now().withNano(0));

        JsonContent<NewCommentDto> result = json.write(newCommentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(newCommentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(newCommentDto.getCreated().toString());
    }

}