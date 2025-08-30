package ru.yandex.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.ewm.model.ParticipationRequest;
import ru.yandex.ewm.model.RequestStatus;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    boolean existsByRequester_IdAndEvent_Id(long requesterId, long eventId);

    long countByEvent_IdAndStatus(long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByRequester_Id(long requesterId);

    List<ParticipationRequest> findAllByEvent_Id(long eventId);

    List<ParticipationRequest> findAllByIdInAndEvent_Id(Collection<Long> ids, long eventId);

    @Modifying
    @Query("update ParticipationRequest r set r.status = :status where r.id in :ids and r.status = 'PENDING'")
    int bulkUpdateStatus(Collection<Long> ids, RequestStatus status);

    @Modifying
    @Query("update ParticipationRequest r set r.status = 'REJECTED' where r.event.id = :eventId and r.status = 'PENDING'")
    int rejectAllPendingForEvent(long eventId);

    @Query("select r.event.id, count(r) " +
            "from ParticipationRequest r " +
            "where r.status = ru.yandex.ewm.model.RequestStatus.CONFIRMED " +
            "  and r.event.id in :eventIds " +
            "group by r.event.id")
    List<Object[]> countConfirmedByEventIds(Collection<Long> eventIds);
}