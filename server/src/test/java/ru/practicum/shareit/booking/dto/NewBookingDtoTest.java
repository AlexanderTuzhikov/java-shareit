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
class NewBookingDtoTest {
    private final JacksonTester<NewBookingDto> json;

    @Test
    void testNewBookingDto() throws Exception {
        NewBookingDto newBookingDto = new NewBookingDto(LocalDateTime.now().withNano(0), LocalDateTime.now().plusHours(1).withNano(0), 1L);

        JsonContent<NewBookingDto> result = json.write(newBookingDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(newBookingDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(newBookingDto.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(newBookingDto.getItemId().intValue());
    }
}