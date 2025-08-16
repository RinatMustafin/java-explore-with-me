package ru.yandex.ewm.dto.event;

import lombok.Value;

import jakarta.validation.constraints.NotNull;

@Value
public class LocationDto {
    @NotNull Float lat;
    @NotNull Float lon;
}
