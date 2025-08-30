package ru.yandex.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.ewm.model.Comment;
import ru.yandex.ewm.model.CommentStatus;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByEventIdAndStatus(Long eventId, CommentStatus status, Pageable pageable);

    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);

    @Query("""
            SELECT c
              FROM Comment c
             WHERE (:eventId IS NULL OR c.event.id = :eventId)
               AND (:authorId IS NULL OR c.author.id = :authorId)
               AND (:status IS NULL OR c.status = :status)
               AND (:start IS NULL OR c.createdOn >= :start)
               AND (:end   IS NULL OR c.createdOn <= :end)
             ORDER BY c.createdOn DESC
            """)
    Page<Comment> adminSearch(@Param("eventId") Long eventId,
                              @Param("authorId") Long authorId,
                              @Param("status") CommentStatus status,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              Pageable pageable);
}
