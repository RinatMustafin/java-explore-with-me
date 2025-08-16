package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.event.*;
import ru.yandex.ewm.exception.ConflictException;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.helper.DateTimeUtils;
import ru.yandex.ewm.mapper.EventMapper;
import ru.yandex.ewm.model.*;
import ru.yandex.ewm.repository.CategoryRepository;
import ru.yandex.ewm.repository.EventRepository;
import ru.yandex.ewm.repository.UserRepository;
import ru.yandex.ewm.service.EventService;
import ru.yandex.ewm.helper.PageRequestUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository events;
    private final UserRepository users;
    private final CategoryRepository categories;

    private static final int MIN_HOURS_BEFORE_EVENT_USER = 2;
    private static final int MIN_HOURS_BEFORE_PUBLISH = 1;

    private void ensureEventDateIsValidForUser(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_EVENT_USER))) {
            throw new ConflictException("Событие должно быть за два часа");
        }
    }

    @Override
    @Transactional
    public EventFullDto create(long userId, NewEventDto dto) {
        ensureEventDateIsValidForUser(dto.getEventDate());
        var initiator = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id=" + userId + " не найден"));
        var category = categories.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category с id=" + dto.getCategory() + " не найден"));

        Event saved = events.save(EventMapper.toEntity(dto, category, initiator));

        return EventMapper.toFullDto(saved, 0, 0);
    }

    @Override
    public List<EventShortDto> getUserEvents(long userId, int from, int size) {
        Pageable page = PageRequestUtil.of(from, size);
        return events.findAllByInitiator_Id(userId, page).getContent().stream()
                .map(e -> EventMapper.toShortDto(e, 0, 0))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEvent(long userId, long eventId) {
        Event e = events.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!e.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found"); // маскируем чужое событие
        }
        return EventMapper.toFullDto(e, 0, 0);
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(long userId, long eventId, UpdateEventUserRequest dto) {
        Event e = events.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!e.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        if (e.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Редактировать нельзя");
        }

        if (dto.getEventDate() != null) {
            ensureEventDateIsValidForUser(dto.getEventDate());
        }
        var newCategory = (dto.getCategory() == null) ? null :
                categories.findById(dto.getCategory())
                        .orElseThrow(() -> new NotFoundException("Category with id=" + dto.getCategory() + " was not found"));

        EventMapper.applyUserUpdate(e, dto, newCategory);


        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case SEND_TO_REVIEW:
                    e.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    e.setState(EventState.CANCELED);
                    break;
            }
        }

        Event saved = events.save(e);
        return EventMapper.toFullDto(saved, 0, 0);
    }

    private EventState parseState(String s) {
        return EventState.valueOf(s.toUpperCase());
    }

    @Override
    public List<EventFullDto> adminSearch(List<Long> users, List<String> states, List<Long> categories,
                                          String rangeStart, String rangeEnd, int from, int size) {
        var start = DateTimeUtils.parseOrNull(rangeStart);
        var end   = DateTimeUtils.parseOrNull(rangeEnd);


        List<EventState> stateEnums = null;
        if (states != null && !states.isEmpty()) {
            stateEnums = states.stream().map(this::parseState).toList();
        }

        boolean usersEmpty = (users == null || users.isEmpty());
        boolean statesEmpty = (stateEnums == null || stateEnums.isEmpty());
        boolean categoriesEmpty = (categories == null || categories.isEmpty());

        var page = PageRequest.of(from / size, size);

        return events.adminSearch(usersEmpty, users, statesEmpty, stateEnums,
                        categoriesEmpty, categories, start, end, page)
                .map(e -> EventMapper.toFullDto(e, 0, 0))
                .getContent();
    }

    @Override
    @Transactional
    public EventFullDto adminUpdate(long eventId, UpdateEventAdminRequest dto) {
        Event e = events.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));


        var newCategory = (dto.getCategory() == null) ? null :
                categories.findById(dto.getCategory())
                        .orElseThrow(() -> new NotFoundException("Category with id=" + dto.getCategory() + " was not found"));

        EventMapper.applyAdminUpdate(e, dto, newCategory);

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT -> {

                    if (e.getState() != EventState.PENDING) {
                        throw new ConflictException("Only pending events can be published");
                    }

                    if (e.getEventDate().isBefore(java.time.LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_PUBLISH))) {
                        throw new ConflictException("Event date must be at least 1 hour after publish time");
                    }
                    e.setState(EventState.PUBLISHED);
                    e.setPublishedOn(java.time.LocalDateTime.now());
                }
                case REJECT_EVENT -> {

                    if (e.getState() == EventState.PUBLISHED) {
                        throw new ConflictException("Published events cannot be rejected");
                    }
                    e.setState(EventState.CANCELED);
                }
            }
        }

        Event saved = events.save(e);
        return EventMapper.toFullDto(saved, 0, 0);
    }
}
