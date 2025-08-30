package ru.yandex.ewm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import ru.yandex.ewm.model.RequestStatus;

import java.time.LocalDateTime;

@Value
public class ParticipationRequestDto {
    Long id;
    Long requester;
    Long event;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    RequestStatus status;
}
