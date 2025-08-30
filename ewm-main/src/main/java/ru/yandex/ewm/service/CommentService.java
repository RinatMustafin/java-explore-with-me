package ru.yandex.ewm.service;

import ru.yandex.ewm.dto.comment.CommentDto;
import ru.yandex.ewm.dto.comment.CommentPublicDto;
import ru.yandex.ewm.dto.comment.NewCommentDto;
import ru.yandex.ewm.dto.comment.UpdateCommentDto;
import ru.yandex.ewm.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentDto add(long userId, long eventId, NewCommentDto dto);

    CommentDto updateOwn(long userId, long commentId, UpdateCommentDto dto);

    void deleteOwn(long userId, long commentId);

    List<CommentPublicDto> getPublicByEvent(long eventId, int from, int size);

    List<CommentDto> adminSearch(Long eventId, Long authorId, CommentStatus status,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                 int from, int size);

    CommentDto publish(long commentId);

    CommentDto reject(long commentId);
}
