package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.events.dto.EventDtoShort;

import java.util.Set;

@Data
public class CompilationDto {
    private Long id;
    private boolean pinned;
    private String title;
    private Set<EventDtoShort> events;
}
