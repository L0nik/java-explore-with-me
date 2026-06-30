package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryDtoPatch;
import ru.practicum.ewm.category.dto.CategoryDtoPost;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto createCategory(CategoryDtoPost categoryData) {
        log.info("CategoryService: создание новой категории (categoryData = {})", categoryData);
        Category category = CategoryMapper.mapCategoryDtoPostToCategory(categoryData);
        categoryRepository.save(category);
        return CategoryMapper.mapCategoryToCategoryDto(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        log.info("CategoryService: удаление категории (categoryId = {})", categoryId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Категория с id = %d не найдена", categoryId))
        );
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryDto patchCategory(Long categoryId, CategoryDtoPatch categoryData) {
        log.info(
                "CategoryService: обновление данных категории (categoryId = {}, categoryData = {})",
                categoryId,
                categoryData
        );
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Категория с id = %d не найдена", categoryId))
        );
        category.setName(categoryData.getName());
        categoryRepository.save(category);
        return CategoryMapper.mapCategoryToCategoryDto(category);
    }

    public Collection<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::mapCategoryToCategoryDto)
                .toList();
    }

    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Категория с id = %d не найдена", categoryId))
        );

        return CategoryMapper.mapCategoryToCategoryDto(category);
    }


}
