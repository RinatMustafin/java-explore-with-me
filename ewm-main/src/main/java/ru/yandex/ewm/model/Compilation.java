package ru.yandex.ewm.model;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "compilations",
        uniqueConstraints = @UniqueConstraint(name = "uq_compilation_title", columnNames = "title"))
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean pinned = false;

    // Пока Event не реализован, держим только таблицу связей. FK добавим позже.
    // Создадим промежуточную таблицу без жёсткой JPA-ссылки на Event:
    @ElementCollection
    @CollectionTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id",
                    foreignKey = @ForeignKey(name = "fk_compilation_events_compilation"))
    )
    @Column(name = "event_id", nullable = false)
    private Set<Long> eventIds = new LinkedHashSet<>();
}
