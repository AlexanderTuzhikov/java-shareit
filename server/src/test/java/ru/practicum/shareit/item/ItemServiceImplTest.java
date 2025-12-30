package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private User anotherUser;
    private Item item;
    private NewItemDto newItemDto;
    private UpdateItemDto updateItemDto;
    private NewCommentDto newCommentDto;

    @BeforeEach
    void setUp() {
        user = new User(null, "Name", "name@mail.ru");
        entityManager.persist(user);

        anotherUser = new User(null, "AnotherUser", "another_user@mail.ru");
        entityManager.persist(anotherUser);

        item = new Item(null, "Name", "Description", true, user, null);
        entityManager.persist(item);

        Booking booking = new Booking(null, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1), item, user, BookingStatus.APPROVED);
        entityManager.persist(booking);

        entityManager.flush();

        newItemDto = new NewItemDto("Name", "Description", true, null);
        updateItemDto = new UpdateItemDto("Update", "Update", false);
        newCommentDto = new NewCommentDto("Text", LocalDateTime.now());
    }

    @Test
    @DisplayName("POST Item")
    void postItem() {
        ItemDto itemDto = itemService.postItem(user.getId(), newItemDto);
        TypedQuery<Item> query = entityManager.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(newItemDto.getName()));
        assertThat(item.getDescription(), equalTo(newItemDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(newItemDto.getAvailable()));
        assertThat(item.getOwner().getId(), equalTo(user.getId()));
    }

    @Test
    @DisplayName("PATCH Item")
    void patchItem() {
        ItemDto itemDto = itemService.postItem(user.getId(), newItemDto);
        ItemDto updatedItemDto = itemService.patchItem(user.getId(), itemDto.getId(), updateItemDto);

        TypedQuery<Item> query = entityManager.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(updatedItemDto.getName()));
        assertThat(item.getDescription(), equalTo(updatedItemDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(updatedItemDto.isAvailable()));
        assertThat(item.getOwner().getId(), equalTo(itemDto.getOwner().getId()));
        assertThrows(ForbiddenActionException.class, () -> itemService.patchItem(user.getId() + 1, itemDto.getId(), updateItemDto));
    }

    @Test
    @DisplayName("DELETE Item")
    void deleteItem() {
        ItemDto itemDto = itemService.postItem(user.getId(), newItemDto);

        assertThrows(ForbiddenActionException.class, () -> itemService.deleteItem(user.getId() + 1, itemDto.getId()));

        itemService.deleteItem(user.getId(), itemDto.getId());

        assertThrows(NotFoundException.class, () -> itemService.getItem(user.getId(), itemDto.getId()));
    }

    @Test
    @DisplayName("GET Item")
    void getItem() {
        ItemDto itemDto = itemService.postItem(user.getId(), newItemDto);
        ItemBookingDto itemDtoFromDb = itemService.getItem(user.getId(), itemDto.getId());

        assertThat(itemDto.getId(), equalTo(itemDtoFromDb.getId()));
        assertThat(itemDto.getName(), equalTo(itemDtoFromDb.getName()));
        assertThat(itemDto.getDescription(), equalTo(itemDtoFromDb.getDescription()));
        assertThat(itemDto.isAvailable(), equalTo(itemDtoFromDb.isAvailable()));
        assertThat(itemDtoFromDb.getLastBooking(), equalTo(null));
        assertThat(itemDtoFromDb.getNextBooking(), equalTo(null));
        assertThat(itemDtoFromDb.getComments(), is(empty()));
    }

    @Test
    @DisplayName("GET Items")
    void getItems() {
        itemService.postItem(user.getId(), newItemDto);
        Page<ItemBookingDto> itemsDtoFromDb = itemService.getItems(user.getId(), 0, 10);

        assertThat(itemsDtoFromDb, notNullValue());
        assertThat(itemsDtoFromDb.getContent(), not(empty()));
        assertThat(itemsDtoFromDb.getSize(), equalTo(10));
    }

    @Test
    @DisplayName("GET SEARCH Items")
    void getItemsSearch() {
        String searchName = newItemDto.getName();
        itemService.postItem(user.getId(), newItemDto);
        Page<ItemDto> itemsSearchName = itemService.getItemsSearch(user.getId(), searchName, 0, 10);

        assertThat(itemsSearchName.getContent(), not(empty()));
        assertThat(itemsSearchName.getSize(), equalTo(10));
        assertThat(itemsSearchName.getContent().getFirst().getName().toUpperCase(), equalTo(searchName.toUpperCase()));

        String searchDescription = newItemDto.getDescription();
        itemService.postItem(user.getId(), newItemDto);
        Page<ItemDto> itemsSearchDescription = itemService.getItemsSearch(user.getId(), searchDescription, 0, 10);

        assertThat(itemsSearchDescription.getContent(), not(empty()));
        assertThat(itemsSearchDescription.getSize(), equalTo(10));
        assertThat(itemsSearchDescription.getContent().getFirst().getDescription().toUpperCase(), equalTo(searchDescription.toUpperCase()));
    }

    @Test
    @DisplayName("POST Comment")
    void postComment() {
        CommentDto commentDto = itemService.postComment(user.getId(), item.getId(), newCommentDto);
        TypedQuery<Comment> query = entityManager.createQuery("Select c from Comment c where c.id = :id", Comment.class);
        Comment comment = query.setParameter("id", commentDto.getId())
                .getSingleResult();

        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getText(), equalTo(newCommentDto.getText()));
        assertThat(comment.getAuthor().getId(), equalTo(user.getId()));
        assertThat(comment.getItem().getId(), equalTo(item.getId()));

        ItemBookingDto itemBookingDto = itemService.getItem(user.getId(), item.getId());
        assertThat(itemBookingDto.getComments(), notNullValue());
        assertThat(itemBookingDto.getComments().getFirst(), equalTo(commentDto));

        assertThrows(BadRequestException.class, () -> itemService.postComment(anotherUser.getId(), item.getId(), newCommentDto));
    }
}