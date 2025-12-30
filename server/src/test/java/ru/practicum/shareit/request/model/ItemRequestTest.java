package ru.practicum.shareit.request.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestTest {
    private final JacksonTester<ItemRequest> json;

    @Test
    void tesItemRequest() throws Exception {
        User user = new User(1L, "User", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "Description",  user, LocalDateTime.now().withNano(0), new ArrayList<>());

        JsonContent<ItemRequest> result = json.write(itemRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequest.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequest.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(itemRequest.getRequestor().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.requestor.name").isEqualTo(itemRequest.getRequestor().getName());
        assertThat(result).extractingJsonPathStringValue("$.requestor.email").isEqualTo(itemRequest.getRequestor().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequest.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }
}