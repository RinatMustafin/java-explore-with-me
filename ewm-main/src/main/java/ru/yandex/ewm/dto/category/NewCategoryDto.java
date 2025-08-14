package ru.yandex.ewm.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;



@Value
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}