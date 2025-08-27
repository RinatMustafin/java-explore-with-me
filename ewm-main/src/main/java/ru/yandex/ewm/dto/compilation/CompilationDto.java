package ru.yandex.ewm.dto.compilation;

import lombok.Value;
import ru.yandex.ewm.dto.event.EventShortDto;

import java.util.List;

@Value
public class CompilationDto {
    Long id;
    String title;
    boolean pinned;
    List<EventShortDto> events;
}