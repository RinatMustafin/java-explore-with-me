package ru.yandex.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.compilation.CompilationDto;
import ru.yandex.ewm.dto.compilation.NewCompilationDto;
import ru.yandex.ewm.dto.compilation.UpdateCompilationRequest;
import ru.yandex.ewm.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService service;

    public static final String ID = "/{compId}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return service.create(dto);
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        service.delete(compId);
    }

    @PatchMapping(ID)
    public CompilationDto update(@PathVariable long compId,
                                 @Valid @RequestBody UpdateCompilationRequest dto) {
        return service.update(compId, dto);
    }
}
