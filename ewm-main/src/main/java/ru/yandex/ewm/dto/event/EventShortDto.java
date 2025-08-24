package ru.yandex.ewm.dto.event;

import lombok.Value;
import ru.yandex.ewm.dto.category.CategoryDtoShort;
import ru.yandex.ewm.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Value
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDtoShort category;
    Integer confirmedRequests;
    LocalDateTime eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Long views;
}
