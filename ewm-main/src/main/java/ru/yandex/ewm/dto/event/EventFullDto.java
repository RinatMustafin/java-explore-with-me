package ru.yandex.ewm.dto.event;

import lombok.Value;
import ru.yandex.ewm.dto.category.CategoryDtoShort;
import ru.yandex.ewm.dto.user.UserShortDto;
import ru.yandex.ewm.model.EventState;

import java.time.LocalDateTime;

@Value
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDtoShort category;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    UserShortDto initiator;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventState state;
    String title;
    Long views;
    LocalDateTime publishedOn;
}
