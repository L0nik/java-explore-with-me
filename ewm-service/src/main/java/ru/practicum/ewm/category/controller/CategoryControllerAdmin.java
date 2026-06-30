package ru.practicum.ewm.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryDtoPatch;
import ru.practicum.ewm.category.dto.CategoryDtoPost;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDtoPost categoryData) {
        log.info("CategoryControllerAdmin: создание новой категории (categoryData = {})", categoryData);
        return categoryService.createCategory(categoryData);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long categoryId) {
        log.info("CategoryControllerAdmin: удаление категории (categoryId = {})", categoryId);
        categoryService.deleteCategory(categoryId);
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto patchCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryDtoPatch categoryData
    ) {
        log.info(
                "CategoryControllerAdmin: обновление данных категории (categoryId = {}, categoryData = {})",
                categoryId,
                categoryData
        );
        return categoryService.patchCategory(categoryId, categoryData);
    }

}
