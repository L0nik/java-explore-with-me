package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Collection;

@Data
public class CompilationDtoPost {

    @NotEmpty
    private Collection<Long> events;

    boolean pinned;

    @NotBlank
    String title;
}
