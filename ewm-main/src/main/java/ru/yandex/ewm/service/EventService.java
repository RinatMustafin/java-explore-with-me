package ru.yandex.ewm.service;

import ru.yandex.ewm.dto.event.*;
import java.util.List;

public interface EventService {

    EventFullDto create(long userId, NewEventDto dto);
    List<EventShortDto> getUserEvents(long userId, int from, int size);
    EventFullDto getUserEvent(long userId, long eventId);
    EventFullDto updateUserEvent(long userId, long eventId, UpdateEventUserRequest dto);


}
