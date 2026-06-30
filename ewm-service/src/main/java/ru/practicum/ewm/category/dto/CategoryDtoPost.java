package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDtoPost {
    @NotBlank(message = "Не заполнено название категории")
    private String name;
}
