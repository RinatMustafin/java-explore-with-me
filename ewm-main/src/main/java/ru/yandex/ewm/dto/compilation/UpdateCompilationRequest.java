package ru.yandex.ewm.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    @Size(min = 1, max = 50)
    String title;

    List<Long> events;

    Boolean pinned;
}
