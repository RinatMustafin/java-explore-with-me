package ru.yandex.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Long views;
}
