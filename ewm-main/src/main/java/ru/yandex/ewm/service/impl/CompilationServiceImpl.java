package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.compilation.CompilationDto;
import ru.yandex.ewm.dto.compilation.NewCompilationDto;
import ru.yandex.ewm.dto.compilation.UpdateCompilationRequest;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.mapper.CompilationMapper;
import ru.yandex.ewm.model.Compilation;
import ru.yandex.ewm.repository.CompilationRepository;
import ru.yandex.ewm.service.CompilationService;
import ru.yandex.ewm.helper.PageRequestUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repo;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        if (repo.existsByTitle(dto.getTitle())) {
            throw new DataIntegrityViolationException("Compilation title must be unique");
        }
        Compilation saved = repo.save(CompilationMapper.toEntity(dto));
        return CompilationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(long compId) {
        Compilation c = repo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        repo.delete(c);
    }

    @Override
    @Transactional
    public CompilationDto update(long compId, UpdateCompilationRequest dto) {
        Compilation c = repo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        if (dto.getTitle() != null) {
            if (dto.getTitle().isBlank()) {
                throw new DataIntegrityViolationException("Compilation title must not be blank");
            }

            if (repo.existsByTitle(dto.getTitle()) && !dto.getTitle().equals(c.getTitle())) {
                throw new DataIntegrityViolationException("Compilation title must be unique");
            }
            c.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            c.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null) {
            c.getEventIds().clear();
            c.getEventIds().addAll(dto.getEvents());
        }
        Compilation saved = repo.save(c);
        return CompilationMapper.toDto(saved);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable page = PageRequestUtil.of(from, size);
        if (pinned == null) {
            return repo.findAll(page).getContent().stream()
                    .map(CompilationMapper::toDto).collect(Collectors.toList());
        }
        return repo.findAllByPinned(pinned, page).getContent().stream()
                .map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(long compId) {
        Compilation c = repo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        return CompilationMapper.toDto(c);
    }
}