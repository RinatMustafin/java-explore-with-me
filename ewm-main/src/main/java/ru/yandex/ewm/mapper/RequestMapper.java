package ru.yandex.ewm.mapper;

import ru.yandex.ewm.dto.request.ParticipationRequestDto;
import ru.yandex.ewm.model.ParticipationRequest;

public class RequestMapper {
    public static ParticipationRequestDto toDto(ParticipationRequest r) {
        return new ParticipationRequestDto(
                r.getId(),
                r.getRequester().getId(),
                r.getEvent().getId(),
                r.getCreated(),
                r.getStatus()
        );
    }
}
