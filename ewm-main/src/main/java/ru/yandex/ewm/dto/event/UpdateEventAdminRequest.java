package ru.yandex.ewm.dto.event;

import lombok.Value;
import ru.yandex.ewm.model.StateActionAdmin;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class UpdateEventAdminRequest {
    Long category;
    @Size(min = 20, max = 2000) String annotation;
    @Size(min = 20, max = 7000) String description;
    @Size(min = 3, max = 120)  String title;
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;

    StateActionAdmin stateAction; // PUBLISH_EVENT | REJECT_EVENT
}
