package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class CompilationDtoPatch {
    private Collection<Long> events;
    Boolean pinned;
    String title;
}
