package ru.yandex.ewm.mapper;

import ru.yandex.ewm.dto.compilation.CompilationDto;
import ru.yandex.ewm.dto.compilation.NewCompilationDto;
import ru.yandex.ewm.dto.event.EventShortDto;
import ru.yandex.ewm.model.Compilation;

import java.util.List;

public class CompilationMapper {
    public static Compilation toEntity(NewCompilationDto dto) {
        Compilation c = new Compilation();
        c.setTitle(dto.getTitle());
        c.setPinned(Boolean.TRUE.equals(dto.getPinned()));
        if (dto.getEvents() != null) {
            c.getEventIds().addAll(dto.getEvents());
        }
        return c;
    }

    public static CompilationDto toDto(Compilation c, List<EventShortDto> events) {
        return new CompilationDto(
                c.getId(),
                c.getTitle(),
                c.isPinned(),
                events
        );
    }
}
