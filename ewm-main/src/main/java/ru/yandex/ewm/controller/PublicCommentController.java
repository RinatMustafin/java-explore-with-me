package ru.yandex.ewm.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.comment.CommentPublicDto;
import ru.yandex.ewm.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
@Validated
public class PublicCommentController {

    private final CommentService service;

    @GetMapping
    public List<CommentPublicDto> getComments(@PathVariable long eventId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.getPublicByEvent(eventId, from, size);
    }
}
