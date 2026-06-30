package ru.practicum.ewm.category;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryDtoPost;

@UtilityClass
public class CategoryMapper {

    public Category mapCategoryDtoPostToCategory(CategoryDtoPost dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public CategoryDto mapCategoryToCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

}
