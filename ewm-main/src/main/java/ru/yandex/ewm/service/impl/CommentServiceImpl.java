package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.comment.CommentDto;
import ru.yandex.ewm.dto.comment.CommentPublicDto;
import ru.yandex.ewm.dto.comment.NewCommentDto;
import ru.yandex.ewm.dto.comment.UpdateCommentDto;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.mapper.CommentMapper;
import ru.yandex.ewm.model.*;
import ru.yandex.ewm.repository.CommentRepository;
import ru.yandex.ewm.repository.EventRepository;
import ru.yandex.ewm.repository.UserRepository;
import ru.yandex.ewm.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    @Transactional
    @Override
    public CommentDto add(long userId, long eventId, NewCommentDto dto) {
        User author = userRepo.findById(userId)
                .orElseThrow(() -> notFound("User", userId));
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> notFound("Event", eventId));

        ensurePublished(event);

        Comment entity = CommentMapper.toNewEntity(dto, event, author, LocalDateTime.now());
        entity = commentRepo.save(entity);
        return CommentMapper.toDto(entity);
    }

    @Transactional
    @Override
    public CommentDto updateOwn(long userId, long commentId, UpdateCommentDto dto) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> notFound("Comment", commentId));
        ensureOwner(userId, c);
        ensureNotDeleted(c);

        c.setText(dto.getText());
        c.setEditedOn(LocalDateTime.now());
        return CommentMapper.toDto(c);
    }

    @Transactional
    @Override
    public void deleteOwn(long userId, long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> notFound("Comment", commentId));
        ensureOwner(userId, c);
        c.setStatus(CommentStatus.DELETED);
        c.setEditedOn(LocalDateTime.now());
    }

    @Override
    public List<CommentPublicDto> getPublicByEvent(long eventId, int from, int size) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        ensurePublished(e);

        PageRequest pr = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        return commentRepo.findByEventIdAndStatus(eventId, CommentStatus.PUBLISHED, pr)
                .stream()
                .map(CommentMapper::toPublicDto)
                .toList();
    }

    @Override
    public List<CommentDto> adminSearch(Long eventId, Long authorId, CommentStatus status,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        int from, int size) {
        PageRequest pr = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        return commentRepo.adminSearch(eventId, authorId, status, rangeStart, rangeEnd, pr)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto publish(long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> notFound("Comment", commentId));
        if (c.getStatus() != CommentStatus.DELETED) {
            c.setStatus(CommentStatus.PUBLISHED);
        }
        return CommentMapper.toDto(c);
    }

    @Transactional
    @Override
    public CommentDto reject(long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> notFound("Comment", commentId));
        if (c.getStatus() != CommentStatus.DELETED) {
            c.setStatus(CommentStatus.REJECTED);
        }
        return CommentMapper.toDto(c);
    }


    private RuntimeException notFound(String message, long id) {
        return new NotFoundException(message + " not found: id=" + id);
    }

    private void ensureOwner(long userId, Comment comment) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("Comment with id=" + comment.getId() + " was not found");
        }
    }

    private void ensureNotDeleted(Comment c) {
        if (c.getStatus() == CommentStatus.DELETED) {
            throw new RuntimeException("Comment deleted");
        }
    }

    private void ensurePublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + event.getId() + " was not found");
        }
    }
}
