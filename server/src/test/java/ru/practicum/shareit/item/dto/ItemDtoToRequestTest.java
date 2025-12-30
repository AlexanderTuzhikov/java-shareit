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
class ItemDtoToRequestTest {
    private final JacksonTester<ItemDtoToRequest> json;

    @Test
    void tesItemDtoToRequest() throws Exception {
        ItemDtoToRequest itemDtoToRequest = new ItemDtoToRequest(1L, "Name", 1L);

        JsonContent<ItemDtoToRequest> result = json.write(itemDtoToRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDtoToRequest.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoToRequest.getName());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(itemDtoToRequest.getOwnerId().intValue());
    }
}