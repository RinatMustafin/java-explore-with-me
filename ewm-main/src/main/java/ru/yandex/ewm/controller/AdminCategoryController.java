package ru.yandex.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewm.dto.category.CategoryDto;
import ru.yandex.ewm.dto.category.NewCategoryDto;
import ru.yandex.ewm.service.CategoryService;



@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Validated
public class AdminCategoryController {

    public static final String ID = "/{catId}";

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto dto) {
        return service.create(dto);
    }

    @PatchMapping(ID)
    public CategoryDto update(@PathVariable long catId,
                              @Valid @RequestBody CategoryDto dto) {
        return service.update(catId, dto);
    }

    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        service.delete(catId);
    }
}
