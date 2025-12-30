package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceImplTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private User anotherUser;
    private ItemRequest itemRequest;
    private NewtItemRequestDto newtItemRequestDto;

    @BeforeEach
    void setUp() {
        user = new User(null, "Name", "name@mail.ru");
        entityManager.persist(user);

        anotherUser = new User(null, "anotherUser", "anotherUser@mail.ru");
        entityManager.persist(anotherUser);

        itemRequest = new ItemRequest(null, "Description", user, LocalDateTime.now(), new ArrayList<>());
        entityManager.persist(itemRequest);

        entityManager.flush();

        newtItemRequestDto = new NewtItemRequestDto("Description", LocalDateTime.now());
    }

    @Test
    @DisplayName("POST Request")
    void postRequest() {
        ItemRequestDto itemRequestDto = itemRequestService.postRequest(user.getId(), newtItemRequestDto);
        TypedQuery<ItemRequest> query = entityManager.createQuery("Select ir from ItemRequest ir where ir.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId())
                .getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(newtItemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(newtItemRequestDto.getCreated()));
        assertThat(itemRequest.getRequestor(), equalTo(user));
    }

    @Test
    @DisplayName("GET Requests")
    void getRequests() {
        List<ItemRequestDto> itemRequestDtoFromDb = itemRequestService.getRequests(user.getId());
        List<ItemRequestDto> anotherUserItemRequestDtoFromDb = itemRequestService.getRequests(anotherUser.getId());

        assertThat(itemRequestDtoFromDb, notNullValue());
        assertThat(itemRequestDtoFromDb, not(empty()));
        assertThat(anotherUserItemRequestDtoFromDb, notNullValue());
        assertThat(anotherUserItemRequestDtoFromDb, is(empty()));
    }

    @Test
    @DisplayName("GET ALL Requests")
    void getAllRequests() {
        List<ItemRequestDto> itemRequestDtoFromDb = itemRequestService.getAllRequests(user.getId());
        List<ItemRequestDto> anotherUserItemRequestDtoFromDb = itemRequestService.getAllRequests(anotherUser.getId());

        assertThat(itemRequestDtoFromDb, notNullValue());
        assertThat(itemRequestDtoFromDb, not(empty()));
        assertThat(anotherUserItemRequestDtoFromDb, notNullValue());
        assertThat(anotherUserItemRequestDtoFromDb, not(empty()));
    }

    @Test
    @DisplayName("GET Request")
    void getRequest() {
        ItemRequestDto itemRequestDtoFromDb = itemRequestService.getRequest(user.getId(), itemRequest.getId());

        assertThat(itemRequest.getId(), equalTo(itemRequestDtoFromDb.getId()));
    }
}