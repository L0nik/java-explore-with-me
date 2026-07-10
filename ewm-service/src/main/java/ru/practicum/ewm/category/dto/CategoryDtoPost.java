package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDtoPost {
    @NotBlank(message = "Не заполнено название категории")
    @Size(max = 50)
    private String name;
}
