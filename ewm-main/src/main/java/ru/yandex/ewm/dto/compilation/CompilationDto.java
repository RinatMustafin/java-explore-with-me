package ru.yandex.ewm.dto.compilation;

import lombok.Value;

import java.util.List;

@Value
public class CompilationDto {
    Long id;
    String title;
    boolean pinned;
    // Пока отдаём списком id. Позже заменим на List<EventShortDto>.
    List<Long> events;
}