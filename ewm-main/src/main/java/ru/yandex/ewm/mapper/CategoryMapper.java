package ru.yandex.ewm.mapper;

import ru.yandex.ewm.dto.category.CategoryDto;
import ru.yandex.ewm.dto.category.NewCategoryDto;
import ru.yandex.ewm.model.Category;

public class CategoryMapper {
    public static Category toEntity(NewCategoryDto dto) {
        Category c = new Category();
        c.setName(dto.getName());
        return c;
    }

    public static CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName());
    }
}
