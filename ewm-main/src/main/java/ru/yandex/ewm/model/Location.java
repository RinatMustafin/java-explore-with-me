package ru.yandex.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Embeddable;

@Getter @Setter @NoArgsConstructor
@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}