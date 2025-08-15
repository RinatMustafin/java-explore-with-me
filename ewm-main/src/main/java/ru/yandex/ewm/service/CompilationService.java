package ru.yandex.ewm.service;

import ru.yandex.ewm.dto.compilation.CompilationDto;
import ru.yandex.ewm.dto.compilation.NewCompilationDto;
import ru.yandex.ewm.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto dto);
    void delete(long compId);
    CompilationDto update(long compId, UpdateCompilationRequest dto);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);
    CompilationDto getById(long compId);
}
