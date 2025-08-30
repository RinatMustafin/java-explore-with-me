package ru.yandex.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.comment.CommentDto;
import ru.yandex.ewm.dto.comment.NewCommentDto;
import ru.yandex.ewm.dto.comment.UpdateCommentDto;
import ru.yandex.ewm.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@Validated
public class PrivateCommentController {

    private final CommentService service;

    public static final String ID = "/comments/{commentId}";

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto add(@PathVariable long userId,
                          @PathVariable long eventId,
                          @Valid @RequestBody NewCommentDto dto) {
        return service.add(userId, eventId, dto);
    }

    @PatchMapping(ID)
    public CommentDto update(@PathVariable long userId,
                             @PathVariable long commentId,
                             @Valid @RequestBody UpdateCommentDto dto) {
        return service.updateOwn(userId, commentId, dto);
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId,
                       @PathVariable long commentId) {
        service.deleteOwn(userId, commentId);
    }
}
