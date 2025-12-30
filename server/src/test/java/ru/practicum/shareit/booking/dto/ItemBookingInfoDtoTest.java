package ru.practicum.shareit.booking.dto;

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
class ItemBookingInfoDtoTest {
    private final JacksonTester<ItemBookingInfoDto> json;

    @Test
    void testItemBookingInfoDto() throws Exception {
        ItemBookingInfoDto itemBookingInfoDto = new ItemBookingInfoDto(LocalDateTime.now().withNano(0), LocalDateTime.now().plusHours(1).withNano(0));

        JsonContent<ItemBookingInfoDto> result = json.write(itemBookingInfoDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(itemBookingInfoDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(itemBookingInfoDto.getEnd().toString());
    }
}