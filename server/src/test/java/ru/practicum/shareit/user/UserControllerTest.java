package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    UserService userService;

    private final String url = "/users";

    private NewUserDto newUserDto;
    private UserDto userDto;
    private UpdateUserDto updateUserDto;
    private UserDto updatedUserDto;

    @BeforeEach
    void setUp() {
        newUserDto = new NewUserDto("User", "user@mail.ru");
        userDto = new UserDto(1L, newUserDto.getName(), newUserDto.getEmail());
        updateUserDto = new UpdateUserDto("UpdateName", "updateEmail@mail.ru");
        updatedUserDto = new UserDto(2L, newUserDto.getName(), newUserDto.getEmail());
        updatedUserDto.setName(updateUserDto.getName());
        updatedUserDto.setEmail(updateUserDto.getEmail());

    }

    @Test
    @DisplayName("POST User")
    void postUser() throws Exception {
        when(userService.postUser(any(NewUserDto.class)))
                .thenReturn(userDto);

        mvc.perform(post(url)
                .content(mapper.writeValueAsString(newUserDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    @DisplayName("PATCH User")
    void patchUser() throws Exception {
        when(userService.patchUser(anyLong(), any(UpdateUserDto.class)))
                .thenReturn(updatedUserDto);

        mvc.perform(patch(url + "/{userId}", updatedUserDto.getId())
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedUserDto.getName()))
                .andExpect(jsonPath("$.email").value(updatedUserDto.getEmail()));
    }

    @Test
    @DisplayName("DELETE User")
    void deleteUser()
            throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mvc.perform(delete(url + "/{userId}", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET User")
    void getUser() throws Exception {
        when(userService.getUser(anyLong()))
                .thenReturn(userDto);

        mvc.perform(get(url + "/{userId}", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    @DisplayName("GET Users")
    void getUsers() throws Exception {
        when(userService.getUsers(anyLong(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(userDto), PageRequest.of(0, 10), 1));

        mvc.perform(get(url)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}