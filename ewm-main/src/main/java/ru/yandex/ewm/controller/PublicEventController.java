package ru.yandex.ewm.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.event.EventFullDto;
import ru.yandex.ewm.dto.event.EventShortDto;
import ru.yandex.ewm.dto.event.EventSort;
import ru.yandex.ewm.service.EventService;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
public class PublicEventController {

    private final EventService service;

    @GetMapping
    public List<EventShortDto> search(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(required = false) Boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = "10") @Positive Integer size,
                                      HttpServletRequest request) {
        return service.publicSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable long id, HttpServletRequest request) {
        return service.getPublishedEvent(id, request);
    }
}
