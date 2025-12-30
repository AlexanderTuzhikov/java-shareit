package ru.practicum.shareit.item.comment.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentTest {
    private final JacksonTester<Comment> json;

    @Test
    void tesComment() throws Exception {
        User user = new User(1L, "User", "user@mail.ru");
        Item item = new Item(1L, "Name", "Description", true, user, null);
        Comment comment = new Comment(1L, "Text", item, user, LocalDateTime.now().withNano(0));

        JsonContent<Comment> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(comment.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(comment.getItem().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(comment.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(comment.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(comment.getItem().isAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id").isEqualTo(comment.getItem().getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name").isEqualTo(comment.getItem().getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email").isEqualTo(comment.getItem().getOwner().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
    }
}