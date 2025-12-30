package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void tesItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Description", LocalDateTime.now().withNano(0), new ArrayList<>());

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequestDto.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }

}