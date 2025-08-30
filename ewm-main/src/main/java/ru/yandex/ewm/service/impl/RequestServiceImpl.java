package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.yandex.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.yandex.ewm.dto.request.ParticipationRequestDto;
import ru.yandex.ewm.exception.ConflictException;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.mapper.RequestMapper;
import ru.yandex.ewm.model.EventState;
import ru.yandex.ewm.model.ParticipationRequest;
import ru.yandex.ewm.model.RequestStatus;
import ru.yandex.ewm.repository.EventRepository;
import ru.yandex.ewm.repository.RequestRepository;
import ru.yandex.ewm.repository.UserRepository;
import ru.yandex.ewm.service.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requests;
    private final UserRepository users;
    private final EventRepository events;


    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        users.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));
        return requests.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(long userId, long eventId) {
        var user = users.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));
        var event = events.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));


        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator cannot request participation in own event");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot participate in an unpublished event");
        }

        if (requests.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictException("Duplicate participation request");
        }

        long confirmed = requests.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() != null && event.getParticipantLimit() > 0
                && confirmed >= event.getParticipantLimit()) {
            throw new ConflictException("Participant limit reached");
        }

        var r = new ParticipationRequest();
        r.setRequester(user);
        r.setEvent(event);
        r.setCreated(LocalDateTime.now());


        boolean auto = Boolean.FALSE.equals(event.getRequestModeration()) ||
                event.getParticipantLimit() == null || event.getParticipantLimit() == 0;

        r.setStatus(auto ? RequestStatus.CONFIRMED : RequestStatus.PENDING);

        var saved = requests.save(r);
        return RequestMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        var req = requests.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request with id=" + requestId + " was not found"));
        if (!req.getRequester().getId().equals(userId)) {

            throw new NotFoundException("Request with id=" + requestId + " was not found");
        }
        req.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(req);
    }



    @Override
    public List<ParticipationRequestDto> getRequestsForOwner(long userId, long eventId) {
        var event = events.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        return requests.findAllByEvent_Id(eventId).stream()
                .map(RequestMapper::toDto).toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatuses(long userId, long eventId, EventRequestStatusUpdateRequest body) {
        var event = events.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }


        var toChange = requests.findAllByIdInAndEvent_Id(body.getRequestIds(), eventId);
        if (toChange.size() != body.getRequestIds().size()) {
            throw new NotFoundException("Some requests not found for this event");
        }


        if (toChange.stream().anyMatch(r -> r.getStatus() != RequestStatus.PENDING)) {
            throw new ConflictException("Only pending requests can be changed");
        }

        var confirmedList = new ArrayList<ParticipationRequestDto>();
        var rejectedList  = new ArrayList<ParticipationRequestDto>();

        String target = body.getStatus() == null ? "" : body.getStatus().toUpperCase();
        switch (target) {
            case "CONFIRMED" -> {
                long confirmed = requests.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
                int limit = (event.getParticipantLimit() == null) ? 0 : event.getParticipantLimit();


                if (limit > 0) {
                    long slots = limit - confirmed;
                    if (slots <= 0) {
                        throw new ConflictException("Participant limit already reached");
                    }

                    for (var r : toChange) {
                        if (slots > 0) {
                            r.setStatus(RequestStatus.CONFIRMED);
                            slots--;
                            confirmedList.add(RequestMapper.toDto(r));
                        } else {
                            r.setStatus(RequestStatus.REJECTED);
                            rejectedList.add(RequestMapper.toDto(r));
                        }
                    }

                    if (limit - confirmed <= toChange.size()) {
                        requests.rejectAllPendingForEvent(eventId);
                    }
                } else {

                    toChange.forEach(r -> {
                        r.setStatus(RequestStatus.CONFIRMED);
                        confirmedList.add(RequestMapper.toDto(r));
                    });
                }
            }
            case "REJECTED" -> {
                toChange.forEach(r -> {
                    r.setStatus(RequestStatus.REJECTED);
                    rejectedList.add(RequestMapper.toDto(r));
                });
            }
            default -> {
                if (!"CONFIRMED".equals(target) && !"REJECTED".equals(target)) {
                    throw new ConflictException("Unsupported status: " + body.getStatus());
                }
            }
        }

        return new EventRequestStatusUpdateResult(confirmedList, rejectedList);
    }
}
