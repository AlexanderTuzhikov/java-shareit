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
class UpdateItemDtoTest {
    private final JacksonTester<UpdateItemDto> json;

    @Test
    void tesUpdateItemDto() throws Exception {
        UpdateItemDto updateItemDto = new UpdateItemDto("Name", "Description", true);

        JsonContent<UpdateItemDto> result = json.write(updateItemDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(updateItemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(updateItemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(updateItemDto.getAvailable());
    }
}