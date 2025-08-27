package ru.yandex.ewm.dto.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull Long category; // id категории

    @NotBlank @Size(min = 20, max = 2000)
    String annotation;

    @NotBlank @Size(min = 20, max = 7000)
    String description;

    @NotBlank @Size(min = 3, max = 120)
    String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;   // default=0
    Boolean requestModeration;  // default=true
}
