package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class, properties = "spring.test.context.aot.enabled=false")
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    BookingService bookingService;

    private final String url = "/bookings";
    private UserDto bookerDto;
    private NewBookingDto newBookingDto;
    private BookingDto bookingDto;
    private BookingDto updatedBookingDto;

    @BeforeEach
    void setUp() {
        UserDto ownerDto = new UserDto(1L, "Owner", "owner@mail.ru");
        bookerDto = new UserDto(2L, "Booker", "booker@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "Item", "Test item", true, ownerDto);
        newBookingDto = new NewBookingDto(LocalDateTime.now(), LocalDateTime.now().plusHours(1), itemDto.getId());
        bookingDto = new BookingDto(1L, newBookingDto.getStart(), newBookingDto.getEnd(), itemDto, bookerDto, BookingStatus.WAITING);
        updatedBookingDto = bookingDto;
        updatedBookingDto.setStatus(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("POST Booking")
    void postBooking() throws Exception {
        when(bookingService.postBooking(anyLong(), any(NewBookingDto.class)))
                .thenReturn(bookingDto);

        mvc.perform(post(url)
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .content(mapper.writeValueAsString(newBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.item.description").value(bookingDto.getItem().getDescription()))
                .andExpect(jsonPath("$.item.available").value(bookingDto.getItem().isAvailable()))
                .andExpect(jsonPath("$.item.owner.id").value(bookingDto.getItem().getOwner().getId()))
                .andExpect(jsonPath("$.item.owner.name").value(bookingDto.getItem().getOwner().getName()))
                .andExpect(jsonPath("$.item.owner.email").value(bookingDto.getItem().getOwner().getEmail()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$.booker.email").value(bookingDto.getBooker().getEmail()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().name()));

    }

    @Test
    @DisplayName("PATCH Booking")
    void patchBooking() throws Exception {
        when(bookingService.patchBooking(anyLong(), anyLong(), any()))
                .thenReturn(updatedBookingDto);

        mvc.perform(patch(url + "/{bookingId}", bookingDto.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value(updatedBookingDto.getStatus().name()));
    }

    @Test
    @DisplayName("GET Booking")
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get(url + "/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    @DisplayName("GET Bookings")
    void getBookings() throws Exception {
        when(bookingService.getBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get(url)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", bookerDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}