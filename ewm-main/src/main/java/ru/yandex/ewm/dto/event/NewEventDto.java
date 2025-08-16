package ru.yandex.ewm.dto.event;


import lombok.Value;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Value
public class NewEventDto {
    @NotNull Long category; // id категории

    @NotBlank @Size(min = 20, max = 2000)
    String annotation;

    @NotBlank @Size(min = 20, max = 7000)
    String description;

    @NotBlank @Size(min = 3, max = 120)
    String title;

    @NotNull
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    Boolean paid;               // default=false
    Integer participantLimit;   // default=0
    Boolean requestModeration;  // default=true
}
