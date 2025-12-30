package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    @Test
    void tesItemDto() throws Exception {
        UserDto userDto = new UserDto(1L, "User", "user@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "Name", "Description", true, userDto);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.isAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(itemDto.getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo(itemDto.getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo(itemDto.getOwner().getEmail());
    }
}