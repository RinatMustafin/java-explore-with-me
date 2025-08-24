package ru.yandex.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.yandex.ewm.dto.event.*;

import java.util.List;

public interface EventService {

    EventFullDto create(long userId, NewEventDto dto);

    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto getUserEvent(long userId, long eventId);

    EventFullDto updateUserEvent(long userId, long eventId, UpdateEventUserRequest dto);

    List<EventFullDto> adminSearch(List<Long> users, List<String> states, List<Long> categories,
                                   String rangeStart, String rangeEnd, int from, int size);

    EventFullDto adminUpdate(long eventId, UpdateEventAdminRequest dto);

    List<EventShortDto> publicSearch(String text, List<Long> categories,
                                     Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable,
                                     EventSort sort, int from, int size, HttpServletRequest request);

    EventFullDto getPublishedEvent(long eventId, HttpServletRequest request);
}
