package ru.yandex.ewm.dto.compilation;

import lombok.Value;

import jakarta.validation.constraints.Size;
import java.util.List;

@Value
public class UpdateCompilationRequest {
    @Size(min = 1, max = 50)
    String title;

    Boolean pinned;

    List<Long> events;
}
