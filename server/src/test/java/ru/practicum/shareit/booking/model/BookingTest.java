package ru.practicum.shareit.booking.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingTest {
    private final JacksonTester<Booking> json;

    @Test
    void testBooking() throws Exception {
        User user = new User(1L, "User", "user@mail.ru");
        Item item = new Item(1L, "Name", "Description", true, user, null);
        Booking booking = new Booking(1L, LocalDateTime.now().withNano(0), LocalDateTime.now().plusHours(1).withNano(0), item, user, BookingStatus.WAITING);

        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(booking.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(booking.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(booking.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(booking.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(booking.getItem().isAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id").isEqualTo(booking.getItem().getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name").isEqualTo(booking.getItem().getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email").isEqualTo(booking.getItem().getOwner().getEmail());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(booking.getBooker().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(booking.getBooker().getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(booking.getBooker().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().name());

    }

}