package ru.practicum.shareit.item.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "item", ignore = true)
    Comment mapToComment(NewCommentDto newCommentDto);

    @Mapping(target = "item", source = "item", qualifiedByName = "mapToItemDto")
    @Mapping(target = "authorName", source = "author.name")
    CommentDto mapToCommentDto(Comment comment);

    @Named("mapToUserDto")
    default UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    @Named("mapToItemDto")
    default ItemDto mapToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(), mapToUserDto(item.getOwner()));
    }
}