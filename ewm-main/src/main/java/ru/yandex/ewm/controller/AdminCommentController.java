package ru.yandex.ewm.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.comment.CommentDto;
import ru.yandex.ewm.model.CommentStatus;
import ru.yandex.ewm.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentController {

    private final CommentService service;

    @GetMapping
    public List<CommentDto> search(@RequestParam(required = false) Long eventId,
                                   @RequestParam(required = false) Long authorId,
                                   @RequestParam(required = false) CommentStatus status,
                                   @RequestParam(required = false)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime rangeStart,
                                   @RequestParam(required = false)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime rangeEnd,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.adminSearch(eventId, authorId, status, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{commentId}/publish")
    public CommentDto publish(@PathVariable long commentId) {
        return service.publish(commentId);
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto reject(@PathVariable long commentId) {
        return service.reject(commentId);
    }
}
