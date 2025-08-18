package ru.yandex.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.yandex.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.yandex.ewm.dto.request.ParticipationRequestDto;
import ru.yandex.ewm.service.RequestService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/requests")
@Validated
public class PrivateEventRequestsController {

    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                                     @PathVariable long eventId) {
        return service.getRequestsForOwner(userId, eventId);
    }

    @PatchMapping
    public EventRequestStatusUpdateResult update(@PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @Valid @RequestBody EventRequestStatusUpdateRequest body) {
        return service.updateStatuses(userId, eventId, body);
    }
}
