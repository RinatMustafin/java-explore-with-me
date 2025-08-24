package ru.yandex.ewm.mapper;

import ru.yandex.ewm.dto.category.CategoryDtoShort;
import ru.yandex.ewm.dto.event.*;
import ru.yandex.ewm.dto.user.UserShortDto;
import ru.yandex.ewm.model.*;

import java.time.LocalDateTime;

public class EventMapper {


    public static Event toEntity(NewEventDto dto, Category category, User initiator) {
        Event e = new Event();
        e.setCategory(category);
        e.setInitiator(initiator);
        e.setAnnotation(dto.getAnnotation());
        e.setDescription(dto.getDescription());
        e.setTitle(dto.getTitle());
        e.setEventDate(dto.getEventDate());
        e.setPaid(Boolean.TRUE.equals(dto.getPaid()));
        e.setParticipantLimit(dto.getParticipantLimit() == null ? 0 : dto.getParticipantLimit());
        e.setRequestModeration(dto.getRequestModeration() == null ? true : dto.getRequestModeration());

        Location loc = new Location();
        loc.setLat(dto.getLocation().getLat());
        loc.setLon(dto.getLocation().getLon());
        e.setLocation(loc);

        e.setCreatedOn(LocalDateTime.now());
        e.setState(EventState.PENDING);
        return e;
    }


    public static EventShortDto toShortDto(Event e, int confirmedRequests, long views) {
        return new EventShortDto(
                e.getId(),
                e.getAnnotation(),
                new CategoryDtoShort(e.getCategory().getId(), e.getCategory().getName()),
                confirmedRequests,
                e.getEventDate(),
                new UserShortDto(e.getInitiator().getId(), e.getInitiator().getName()),
                e.getPaid(),
                e.getTitle(),
                views
        );
    }

    public static EventFullDto toFullDto(Event e, int confirmedRequests, long views) {
        LocationDto loc = new LocationDto(e.getLocation().getLat(), e.getLocation().getLon());
        return new EventFullDto(
                e.getId(),
                e.getAnnotation(),
                new CategoryDtoShort(e.getCategory().getId(), e.getCategory().getName()),
                confirmedRequests,
                e.getCreatedOn(),
                e.getDescription(),
                e.getEventDate(),
                new UserShortDto(e.getInitiator().getId(), e.getInitiator().getName()),
                loc,
                e.getPaid(),
                e.getParticipantLimit(),
                e.getRequestModeration(),
                e.getState(),
                e.getTitle(),
                views,
                e.getPublishedOn()
        );
    }

    public static void applyUserUpdate(Event e, UpdateEventUserRequest dto, Category newCategoryOrNull) {
        if (dto.getAnnotation() != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getTitle() != null) e.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) e.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) e.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) e.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) e.setRequestModeration(dto.getRequestModeration());
        if (dto.getLocation() != null) {
            e.getLocation().setLat(dto.getLocation().getLat());
            e.getLocation().setLon(dto.getLocation().getLon());
        }
        if (newCategoryOrNull != null) e.setCategory(newCategoryOrNull);
    }

    public static void applyAdminUpdate(Event e, UpdateEventAdminRequest dto, Category newCategoryOrNull) {
        if (dto.getAnnotation() != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getTitle() != null) e.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) e.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) e.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) e.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) e.setRequestModeration(dto.getRequestModeration());
        if (dto.getLocation() != null) {
            e.getLocation().setLat(dto.getLocation().getLat());
            e.getLocation().setLon(dto.getLocation().getLon());
        }
        if (newCategoryOrNull != null) e.setCategory(newCategoryOrNull);
    }
}
