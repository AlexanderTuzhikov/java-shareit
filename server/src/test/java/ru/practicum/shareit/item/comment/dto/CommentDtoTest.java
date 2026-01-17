package ru.practicum.shareit.item.comment.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void tesCommentDto() throws Exception {
        UserDto userDto = new UserDto(1L, "User", "user@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "Name", "Description", true, userDto);
        CommentDto commentDto = new CommentDto(1L, "Text", itemDto, userDto.getName(), LocalDateTime.now().withNano(0));

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(commentDto.getItem().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(commentDto.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(commentDto.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(commentDto.getItem().isAvailable());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(commentDto.getCreated().toString());
    }
}