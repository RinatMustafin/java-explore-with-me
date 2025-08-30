package ru.yandex.ewm.service;

import ru.yandex.ewm.dto.category.CategoryDto;
import ru.yandex.ewm.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto dto);

    CategoryDto update(long catId, CategoryDto dto);

    void delete(long catId);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(long catId);
}
