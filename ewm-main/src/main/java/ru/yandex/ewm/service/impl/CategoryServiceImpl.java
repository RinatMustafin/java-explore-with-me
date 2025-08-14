package ru.yandex.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.ewm.dto.category.CategoryDto;
import ru.yandex.ewm.dto.category.NewCategoryDto;
import ru.yandex.ewm.exception.NotFoundException;
import ru.yandex.ewm.mapper.CategoryMapper;
import ru.yandex.ewm.model.Category;
import ru.yandex.ewm.repository.CategoryRepository;
import ru.yandex.ewm.service.CategoryService;
import ru.yandex.ewm.pageable.PageRequestUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto dto) {

        repo.findByName(dto.getName()).ifPresent(c -> {
            throw new DataIntegrityViolationException("Category name must be unique");
        });
        Category saved = repo.save(CategoryMapper.toEntity(dto));
        return CategoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CategoryDto update(long catId, CategoryDto dto) {
        Category c = repo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        if (dto.getName() != null && !dto.getName().isBlank()) {
            c.setName(dto.getName());
        }
        Category saved = repo.save(c);
        return CategoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(long catId) {
        Category c = repo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        repo.delete(c);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pageable page = PageRequestUtil.of(from, size);
        return repo.findAll(page).getContent()
                .stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long catId) {
        Category c = repo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        return CategoryMapper.toDto(c);
    }
}
