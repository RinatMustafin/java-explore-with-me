package ru.yandex.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewm.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
