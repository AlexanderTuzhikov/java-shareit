package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class, properties = "spring.test.context.aot.enabled=false")
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ItemService itemService;

    private final String url = "/items";

    private UserDto ownerDto;
    private NewItemDto newItemDto;
    private UpdateItemDto updateItemDto;
    private ItemDto itemDto;
    private ItemDto updatedItemDto;
    private ItemBookingDto itemBookingDto;
    private NewCommentDto newCommentDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        ownerDto = new UserDto(1L, "Owner", "owner@mail.ru");
        newItemDto = new NewItemDto("Item", "Test item", true, null);
        itemDto = new ItemDto(1L, newItemDto.getName(), newItemDto.getDescription(), newItemDto.getAvailable(), ownerDto);
        updateItemDto = new UpdateItemDto("Update name", "Update description", false);
        updatedItemDto = new ItemDto(2L, newItemDto.getName(), newItemDto.getDescription(), newItemDto.getAvailable(), ownerDto);
        updateItemDto.setName(updateItemDto.getName());
        updateItemDto.setDescription(updateItemDto.getDescription());
        updateItemDto.setAvailable(updateItemDto.getAvailable());
        itemBookingDto = new ItemBookingDto(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.isAvailable(), null, null, new ArrayList<>());
        newCommentDto = new NewCommentDto("Comment", LocalDateTime.now());
        commentDto = new CommentDto(1L, newCommentDto.getText(), itemDto, ownerDto.getName(), newCommentDto.getCreated());
    }

    @Test
    @DisplayName("POST Item")
    void postItem() throws Exception {
        when(itemService.postItem(anyLong(), any(NewItemDto.class)))
                .thenReturn(itemDto);

        mvc.perform(post(url)
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .content(mapper.writeValueAsString(newItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.isAvailable()))
                .andExpect(jsonPath("$.owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$.owner.email").value(itemDto.getOwner().getEmail()));
    }

    @Test
    @DisplayName("PATCH Item")
    void patchItem() throws Exception {
        when(itemService.patchItem(anyLong(), anyLong(), any(UpdateItemDto.class)))
                .thenReturn(updatedItemDto);

        mvc.perform(patch(url + "/{itemId}", updatedItemDto.getId())
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .content(mapper.writeValueAsString(updateItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedItemDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedItemDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.isAvailable()));
    }

    @Test
    @DisplayName("DELETE Item")
    void deleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(anyLong(), anyLong());

        mvc.perform(delete(url + "/{itemId}", itemDto.getId())
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET Item")
    void getItem() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemBookingDto);

        mvc.perform(get(url + "/{itemId}", itemDto.getId())
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemBookingDto.getId()))
                .andExpect(jsonPath("$.name").value(itemBookingDto.getName()))
                .andExpect(jsonPath("$.description").value(itemBookingDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemBookingDto.isAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(isEmptyOrNullString()))
                .andExpect(jsonPath("$.nextBooking").value(isEmptyOrNullString()))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments").isEmpty());

    }

    @Test
    @DisplayName("GET Items")
    void getItems() throws Exception {
        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(itemBookingDto), PageRequest.of(0, 10), 1));

        mvc.perform(get(url)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("GET Items SEARCH")
    void getItemsSearch() throws Exception {
        when(itemService.getItemsSearch(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(itemDto), PageRequest.of(0, 10), 1));

        mvc.perform(get(url + "/search")
                        .param("text", itemDto.getName())
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("POST Comment")
    void postComment() throws Exception {
        when(itemService.postComment(anyLong(), anyLong(), any(NewCommentDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post(url + "/{itemId}/comment", commentDto.getItem().getId())
                        .header("X-Sharer-User-Id", ownerDto.getId())
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.item.id").value(commentDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(commentDto.getItem().getName()))
                .andExpect(jsonPath("$.item.description").value(commentDto.getItem().getDescription()))
                .andExpect(jsonPath("$.item.available").value(commentDto.getItem().isAvailable()))
                .andExpect(jsonPath("$.item.owner.id").value(commentDto.getItem().getOwner().getId()))
                .andExpect(jsonPath("$.item.owner.name").value(commentDto.getItem().getOwner().getName()))
                .andExpect(jsonPath("$.item.owner.email").value(commentDto.getItem().getOwner().getEmail()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.created").exists());
    }
}