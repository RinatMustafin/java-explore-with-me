package ru.yandex.ewm.dto.request;

import lombok.Value;
import ru.yandex.ewm.model.RequestStatus;

import java.time.LocalDateTime;

@Value
public class ParticipationRequestDto {
    Long id;
    Long requester;
    Long event;
    LocalDateTime created;
    RequestStatus status;
}
