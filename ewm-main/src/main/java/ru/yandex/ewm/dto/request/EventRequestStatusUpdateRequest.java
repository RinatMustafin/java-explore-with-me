package ru.yandex.ewm.dto.request;

import lombok.Value;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Value
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    List<Long> requestIds;
    String status;
}
