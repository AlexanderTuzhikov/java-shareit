package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class NewItemDtoTest {
    private final JacksonTester<NewItemDto> json;

    @Test
    void tesNewItemDto() throws Exception {
        NewItemDto newItemDto = new NewItemDto("Name", "Description", true, 1L);

        JsonContent<NewItemDto> result = json.write(newItemDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(newItemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(newItemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(newItemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(newItemDto.getRequestId().intValue());
    }
}