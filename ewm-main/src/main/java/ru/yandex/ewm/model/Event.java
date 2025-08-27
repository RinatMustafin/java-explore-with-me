package ru.yandex.ewm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events",
        indexes = {
                @Index(name = "idx_event_state", columnList = "state"),
                @Index(name = "idx_event_category", columnList = "category_id"),
                @Index(name = "idx_event_date", columnList = "event_date"),
                @Index(name = "idx_event_paid", columnList = "paid")
        })
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_event_user"))
    private User initiator;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_event_category"))
    private Category category;

    @Column(columnDefinition = "text", length = 2000, nullable = false)
    private String annotation;

    @Column(columnDefinition = "text", length = 7000, nullable = false)
    private String description;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private Boolean paid = false;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit = 0;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration = true;

    @Embedded
    private Location location;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state = EventState.PENDING;
}
