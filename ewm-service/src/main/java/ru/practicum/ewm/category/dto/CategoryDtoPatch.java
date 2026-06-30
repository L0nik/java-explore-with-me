package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDtoPatch {
    @NotBlank(message = "Не заполнено название категории")
    private String name;
}
