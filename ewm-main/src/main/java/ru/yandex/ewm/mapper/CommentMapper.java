package ru.yandex.ewm.mapper;

import ru.yandex.ewm.dto.comment.CommentDto;
import ru.yandex.ewm.dto.comment.CommentPublicDto;
import ru.yandex.ewm.dto.comment.NewCommentDto;
import ru.yandex.ewm.dto.user.UserShortDto;
import ru.yandex.ewm.model.Comment;
import ru.yandex.ewm.model.CommentStatus;
import ru.yandex.ewm.model.Event;
import ru.yandex.ewm.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toDto(Comment c) {
        return new CommentDto(
                c.getId(),
                c.getText(),
                c.getEvent().getId(),
                new UserShortDto(c.getAuthor().getId(), c.getAuthor().getName()),
                c.getCreatedOn(),
                c.getEditedOn(),
                c.getStatus()
        );
    }

    public static Comment toNewEntity(NewCommentDto dto, Event event, User author, LocalDateTime now) {
        return Comment.builder()
                .text(dto.getText())
                .event(event)
                .author(author)
                .createdOn(now)
                .status(CommentStatus.PUBLISHED)
                .build();
    }

    public static CommentPublicDto toPublicDto(Comment c) {
        return new CommentPublicDto(
                c.getId(),
                c.getText(),
                new UserShortDto(c.getAuthor().getId(), c.getAuthor().getName()),
                c.getCreatedOn(),
                c.getEditedOn()
        );
    }
}
