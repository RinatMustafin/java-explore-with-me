package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.compilation.CompilationDto;
import ru.yandex.ewm.dto.compilation.NewCompilationDto;
import ru.yandex.ewm.dto.compilation.UpdateCompilationRequest;
import ru.yandex.ewm.dto.event.EventShortDto;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.helper.PageRequestUtil;
import ru.yandex.ewm.mapper.CompilationMapper;
import ru.yandex.ewm.mapper.EventMapper;
import ru.yandex.ewm.model.Compilation;
import ru.yandex.ewm.model.Event;
import ru.yandex.ewm.repository.CompilationRepository;
import ru.yandex.ewm.repository.EventRepository;
import ru.yandex.ewm.repository.RequestRepository;
import ru.yandex.ewm.service.CompilationService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repo;
    private final EventRepository eventRepo;
    private final RequestRepository requests;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        if (repo.existsByTitle(dto.getTitle())) {
            throw new DataIntegrityViolationException("Compilation title must be unique");
        }
        Compilation saved = repo.save(CompilationMapper.toEntity(dto));
        return toDtoWithEvents(saved);
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
        return toDtoWithEvents(saved);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable page = PageRequestUtil.of(from, size);
        var pageResult = (pinned == null)
                ? repo.findAll(page)
                : repo.findAllByPinned(pinned, page);

        return pageResult.getContent().stream()
                .map(this::toDtoWithEvents)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(long compId) {
        Compilation c = repo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        return toDtoWithEvents(c);
    }


    private CompilationDto toDtoWithEvents(Compilation c) {
        List<Long> idsInOrder = new ArrayList<>(c.getEventIds());
        if (idsInOrder.isEmpty()) {
            return CompilationMapper.toDto(c, Collections.emptyList());
        }

        List<Event> events = eventRepo.findAllByIdIn(idsInOrder);
        Map<Long, Event> byId = events.stream()
                .collect(Collectors.toMap(Event::getId, e -> e));

        Map<Long, Integer> confirmedMap = getConfirmedMap(idsInOrder);

        List<EventShortDto> eventDtos = idsInOrder.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .map(e -> EventMapper.toShortDto(e, confirmedMap.getOrDefault(e.getId(), 0), 0L))
                .collect(Collectors.toList());

        return CompilationMapper.toDto(c, eventDtos);
    }

    private Map<Long, Integer> getConfirmedMap(Collection<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) return Collections.emptyMap();
        var rows = requests.countConfirmedByEventIds(eventIds);
        var map = new HashMap<Long, Integer>(rows.size());
        for (Object[] row : rows) {
            Long id = (Long) row[0];
            Long cnt = (Long) row[1];
            map.put(id, cnt.intValue());
        }
        return map;
    }
}