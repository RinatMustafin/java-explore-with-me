package ru.yandex.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventState state;
    String title;
    Long views;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
}
