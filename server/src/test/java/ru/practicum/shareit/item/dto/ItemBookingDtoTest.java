package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.ItemBookingInfoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemBookingDtoTest {
    private final JacksonTester<ItemBookingDto> json;

    @Test
    void testItemBookingDto() throws Exception {
        ItemBookingInfoDto lastBooking = new ItemBookingInfoDto(LocalDateTime.now().withNano(0), LocalDateTime.now().plusHours(1).withNano(0));
        ItemBookingDto itemBookingDto = new ItemBookingDto(1L, "Name", "Description", true, lastBooking, null, new ArrayList<>());

        JsonContent<ItemBookingDto> result = json.write(itemBookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemBookingDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemBookingDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemBookingDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemBookingDto.isAvailable());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo(itemBookingDto.getLastBooking().getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end").isEqualTo(itemBookingDto.getLastBooking().getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end").isNull();
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEmpty();
    }
}