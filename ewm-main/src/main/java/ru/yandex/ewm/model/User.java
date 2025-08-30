package ru.yandex.ewm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_email", columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250, nullable = false)
    private String name;

    @Column(length = 254, nullable = false)
    private String email;
}
