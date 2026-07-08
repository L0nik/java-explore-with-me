package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CompilationDtoPost {

    @NotEmpty
    private Set<Long> events;

    private boolean pinned;

    @NotBlank
    @Size(max = 50)
    private String title;
}
