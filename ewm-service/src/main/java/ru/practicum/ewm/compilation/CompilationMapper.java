package ru.practicum.ewm.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationDtoPatch;
import ru.practicum.ewm.compilation.dto.CompilationDtoPost;
import ru.practicum.ewm.events.Event;
import ru.practicum.ewm.events.dto.EventDtoShort;

import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public Compilation mapCompilationDtoPostToCompilation(CompilationDtoPost compilationData, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationData.isPinned());
        compilation.setTitle(compilationData.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

    public CompilationDto mapCompilationToCompilationDto(Compilation compilation, Set<EventDtoShort> events) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setPinned(compilation.isPinned());
        dto.setTitle(compilation.getTitle());
        dto.setEvents(events);
        return dto;
    }

    public void updateCompilation(Compilation compilation, CompilationDtoPatch dto, Set<Event> events) {

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            compilation.setTitle(dto.getTitle());
        }

        if (events != null) {
            compilation.setEvents(events);
        }

    }

}
