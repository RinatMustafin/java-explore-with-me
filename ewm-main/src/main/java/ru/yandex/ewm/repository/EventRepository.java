package ru.yandex.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.ewm.model.Event;
import ru.yandex.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    long countByCategory_Id(long categoryId);

    Page<Event> findAllByInitiator_Id(long initiatorId, Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(
            Collection<Long> users, Collection<EventState> states, Collection<Long> categories,
            LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("""
           select e from Event e
           where (:usersEmpty = true or e.initiator.id in :users)
             and (:statesEmpty = true or e.state in :states)
             and (:categoriesEmpty = true or e.category.id in :categories)
              and e.eventDate >= :start
              and e.eventDate <= :end
           """)
    Page<Event> adminSearch(boolean usersEmpty,
                            Collection<Long> users,
                            boolean statesEmpty,
                            Collection<EventState> states,
                            boolean categoriesEmpty,
                            Collection<Long> categories,
                            LocalDateTime start,
                            LocalDateTime end,
                            Pageable pageable);


    @Query("""
           select e from Event e
           where e.state = ru.yandex.ewm.model.EventState.PUBLISHED
              and (
                   :pattern is null
                or lower(e.annotation)  like :pattern
                or lower(e.description) like :pattern
                or lower(e.title)       like :pattern
              )
             and (:paid is null or e.paid = :paid)
             and (:categoryIdsEmpty = true or e.category.id in (:categoryIds))
             and e.eventDate between :start and :end
            order by e.eventDate asc
           """)
    Page<Event> publicSearch(@Param("pattern") String pattern,
                             @Param("paid") Boolean paid,
                             @Param("categoryIdsEmpty") boolean categoryIdsEmpty,
                             @Param("categoryIds") Collection<Long> categoryIds,
                             @Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             Pageable pageable);

    List<Event> findAllByIdIn(Collection<Long> ids);
}
