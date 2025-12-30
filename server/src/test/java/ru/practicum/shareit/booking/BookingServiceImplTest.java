package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Item item;
    private Item itemNotAvailable;
    private Booking booking;
    private NewBookingDto newBookingDto;

    @BeforeEach
    void setUp() {
        user = new User(null, "Name", "name@mail.ru");
        entityManager.persist(user);

        item = new Item(null, "Item", "Description", true, user, null);
        entityManager.persist(item);

        itemNotAvailable = new Item(null, "Item", "Description", false, user, null);
        entityManager.persist(itemNotAvailable);

        booking = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user, BookingStatus.WAITING);
        entityManager.persist(booking);

        entityManager.flush();

        newBookingDto = new NewBookingDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                item.getId());
    }

    @Test
    @DisplayName("CREATE NEW Booking")
    void createNewBooking() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);

        Booking testBooking = new Booking(1L, startTime, endTime, item, user, BookingStatus.WAITING);

        assertThat(testBooking.getId(), equalTo(1L));
        assertThat(startTime, equalTo(testBooking.getStart()));
        assertThat(endTime, equalTo(testBooking.getEnd()));
        assertThat(testBooking.getItem(), equalTo(item));
        assertThat(testBooking.getBooker(), equalTo(user));
        assertThat(testBooking.getStatus(), equalTo(BookingStatus.WAITING));

        testBooking.setStart(startTime.plusHours(1));
        assertThat(startTime.plusHours(1), equalTo(testBooking.getStart()));

        testBooking.setEnd(endTime.plusHours(1));
        assertThat(endTime.plusHours(1), equalTo(testBooking.getEnd()));

        testBooking.setItem(new Item(2L, "Name", "Description", false, user, null));
        assertThat(2L, equalTo(testBooking.getItem().getId()));

        testBooking.setBooker(new User(2L, "Name", "name@mail.ru"));
        assertThat(2L, equalTo(testBooking.getBooker().getId()));

        testBooking.setStatus(BookingStatus.APPROVED);
        assertThat(BookingStatus.APPROVED, equalTo(testBooking.getStatus()));
    }

    @Test
    @DisplayName("POST Booking")
    void postBooking() {
        BookingDto bookingDto = bookingService.postBooking(user.getId(), newBookingDto);
        TypedQuery<Booking> query = entityManager.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(newBookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(newBookingDto.getEnd()));
        assertThat(booking.getItem(), equalTo(item));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThrows(ConflictException.class, () -> bookingService.postBooking(user.getId(), new NewBookingDto(LocalDateTime.now(), LocalDateTime.now().plusHours(1), itemNotAvailable.getId())));
    }

    @Test
    @DisplayName("PATCH Booking")
    void patchBooking() {
        BookingDto bookingDto = bookingService.patchBooking(user.getId(), booking.getId(), true);
        TypedQuery<Booking> query = entityManager.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(booking.getStatus(), equalTo(BookingStatus.APPROVED));
        assertThrows(ForbiddenActionException.class, () -> bookingService.patchBooking(user.getId() + 1, booking.getId(), true));
    }

    @Test
    @DisplayName("GET Booking")
    void getBooking() {
        BookingDto bookingDtoFromDb = bookingService.getBooking(user.getId(), booking.getId());

        assertThat(booking.getId(), equalTo(bookingDtoFromDb.getId()));
        assertThrows(ForbiddenActionException.class, () -> bookingService.getBooking(user.getId() + 1, booking.getId()));
    }

    @Test
    @DisplayName("GET Bookings")
    void getBookings() {
        List<BookingDto> bookingDtoFromDb = bookingService.getBookings(user.getId(), BookingState.ALL.name(), 0, 10);

        assertThat(bookingDtoFromDb.size(), equalTo(1));
    }
}