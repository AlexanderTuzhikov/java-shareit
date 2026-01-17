package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class, properties = "spring.test.context.aot.enabled=false")
class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ItemRequestService itemRequestService;

    private final String url = "/requests";

    private UserDto requestor;
    private NewtItemRequestDto newtItemRequestDto;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        requestor = new UserDto(1L, "Requestor", "requestor@mail.ru");
        newtItemRequestDto = new NewtItemRequestDto("Description", LocalDateTime.now());
        itemRequestDto = new ItemRequestDto(1L, newtItemRequestDto.getDescription(), newtItemRequestDto.getCreated(), new ArrayList<>());
    }

    @Test
    @DisplayName("POST ItemRequest")
    void postRequest() throws Exception {
        when(itemRequestService.postRequest(anyLong(), any(NewtItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mvc.perform(post(url)
                        .header("X-Sharer-User-Id", requestor.getId())
                        .content(mapper.writeValueAsString(newtItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @DisplayName("GET USER ItemRequests")
    void getRequests() throws Exception {
        when(itemRequestService.getRequests(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get(url)
                        .header("X-Sharer-User-Id", requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET ALL ItemRequests")
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get(url + "/all")
                        .header("X-Sharer-User-Id", requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET ItemRequest")
    void getRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get(url + "/{requestId}", itemRequestDto.getId())
                        .header("X-Sharer-User-Id", requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());
    }
}