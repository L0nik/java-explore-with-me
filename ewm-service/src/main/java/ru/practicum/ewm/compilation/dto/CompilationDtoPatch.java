package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CompilationDtoPatch {
    private Set<Long> events;
    private Boolean pinned;

    @Size(max = 50)
    private String title;
}
