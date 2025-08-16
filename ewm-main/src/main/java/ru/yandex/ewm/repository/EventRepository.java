package ru.yandex.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    // Публичный поиск (через JPQL — регистронезависимый поиск по annotation/description/title)
    @Query("""
           select e from Event e
           where e.state = ru.yandex.ewm.model.EventState.PUBLISHED
             and (:text is null or lower(e.annotation) like lower(concat('%', :text, '%'))
                  or lower(e.description) like lower(concat('%', :text, '%'))
                  or lower(e.title) like lower(concat('%', :text, '%')))
             and (:paid is null or e.paid = :paid)
             and (:categoryIdsEmpty = true or e.category.id in (:categoryIds))
             and e.eventDate between :start and :end
           """)
    Page<Event> publicSearch(String text,
                             Boolean paid,
                             boolean categoryIdsEmpty,
                             Collection<Long> categoryIds,
                             LocalDateTime start,
                             LocalDateTime end,
                             Pageable pageable);

    List<Event> findAllByIdIn(Collection<Long> ids);
}
