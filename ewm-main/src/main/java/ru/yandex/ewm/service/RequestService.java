package ru.yandex.ewm.service;

import ru.yandex.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.yandex.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.yandex.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {


    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto addRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);

    List<ParticipationRequestDto> getRequestsForOwner(long userId, long eventId);

    EventRequestStatusUpdateResult updateStatuses(long userId, long eventId, EventRequestStatusUpdateRequest body);
}
