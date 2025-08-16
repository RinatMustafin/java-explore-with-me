package ru.yandex.ewm.dto.event;

import lombok.Value;
import ru.yandex.ewm.model.StateActionUser;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class UpdateEventUserRequest {
    Long category;
    @Size(min = 20, max = 2000) String annotation;
    @Size(min = 20, max = 7000) String description;
    @Size(min = 3, max = 120)  String title;
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    StateActionUser stateAction; // SEND_TO_REVIEW | CANCEL_REVIEW
}
