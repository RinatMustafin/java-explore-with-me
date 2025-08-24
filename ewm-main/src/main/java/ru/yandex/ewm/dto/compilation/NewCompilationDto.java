package ru.yandex.ewm.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;


import java.util.List;

@Value
public class NewCompilationDto {
    @NotBlank
    @Size(min = 1, max = 50)
    String title;


    Boolean pinned;


    List<Long> events;
}
