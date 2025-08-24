package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.ViewStats;
import ru.practicum.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("""
               SELECT new ru.practicum.stats.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip))
               FROM Hit h
               WHERE h.timestamp BETWEEN :start AND :end
               GROUP BY h.app, h.uri
               ORDER BY COUNT(DISTINCT h.ip) DESC
            """)
    List<ViewStats> getUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("""
               SELECT new ru.practicum.stats.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip))
               FROM Hit h
               WHERE h.timestamp BETWEEN :start AND :end
                 AND h.uri IN :uris
               GROUP BY h.app, h.uri
               ORDER BY COUNT(DISTINCT h.ip) DESC
            """)
    List<ViewStats> getUniqueStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
               SELECT new ru.practicum.stats.ViewStats(h.app, h.uri, COUNT(h))
               FROM Hit h
               WHERE h.timestamp BETWEEN :start AND :end
               GROUP BY h.app, h.uri
               ORDER BY COUNT(h) DESC
            """)
    List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("""
               SELECT new ru.practicum.stats.ViewStats(h.app, h.uri, COUNT(h))
               FROM Hit h
               WHERE h.timestamp BETWEEN :start AND :end
                 AND h.uri IN :uris
               GROUP BY h.app, h.uri
               ORDER BY COUNT(h) DESC
            """)
    List<ViewStats> getAllStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
