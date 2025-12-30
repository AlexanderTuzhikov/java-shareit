package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemTest {
    private final JacksonTester<Item> json;

    @Test
    void tesItem() throws Exception {
        User user = new User(1L, "User", "user@mail.ru");
        Item item = new Item(1L, "Name", "Description", true, user, null);

        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.isAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(item.getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo(item.getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo(item.getOwner().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.request").isNull();
    }
}