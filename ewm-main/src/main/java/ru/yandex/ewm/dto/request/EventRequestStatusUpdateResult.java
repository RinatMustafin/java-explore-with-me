package ru.yandex.ewm.dto.request;

import lombok.Value;

import java.util.List;

@Value
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
