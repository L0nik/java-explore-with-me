package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationDtoPatch;
import ru.practicum.ewm.compilation.dto.CompilationDtoPost;
import ru.practicum.ewm.events.Event;
import ru.practicum.ewm.events.EventEnricher;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.dto.EventDtoShort;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final EventEnricher eventEnricher;

    @Transactional
    public CompilationDto createCompilation(CompilationDtoPost compilationData) {

        log.info(
                "CompilationService: создание компиляции событий администратором (compilationData = {})",
                compilationData
        );

        Set<Long> eventIds = compilationData.getEvents();
        Set<Event> events = eventRepository.findByIdIn(eventIds);

        Compilation compilation = CompilationMapper.mapCompilationDtoPostToCompilation(compilationData, events);

        compilationRepository.save(compilation);

        Set<EventDtoShort> eventsDtos = eventEnricher.toShortEventDtos(events);

        return CompilationMapper.mapCompilationToCompilationDto(compilation, eventsDtos);
    }

    @Transactional
    public void deleteCompilation(Long compilationId) {

        log.info(
                "CompilationService: удаление компиляции событий администратором (compilationId = {})",
                compilationId
        );

        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Compilation with id (compilationId = %d) was not found",
                        compilationId
                ))
        );

        compilationRepository.delete(compilation);

    }

    @Transactional
    public CompilationDto patchCompilation(Long compilationId, CompilationDtoPatch compilationData) {

        log.info(
                """
                        CompilationService: изменение компиляции событий администратором
                        (compilationId = {}, compilationData = {})
                """,
                compilationId,
                compilationData
        );

        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Compilation with id (compilationId = %d) was not found",
                        compilationId
                ))
        );

        Set<Event> events = null;

        if (compilationData.getEvents() != null) {
            events =  new HashSet<>(eventRepository.findAllById(compilationData.getEvents()));
        }

        CompilationMapper.updateCompilation(compilation, compilationData, events);

        compilationRepository.save(compilation);

        return CompilationMapper.mapCompilationToCompilationDto(
                compilation,
                eventEnricher.toShortEventDtos(compilation.getEvents())
        );
    }

    public Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size) {

        log.info("""
                CompilationService: получение списка компиляций событий
                (pinned = {}, from = {}, size = {})
                """,
                pinned,
                from,
                size
        );

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> compilationsLazy;

        if (pinned == null) {
            compilationsLazy = compilationRepository.findAll(pageable);
        } else {
            compilationsLazy = compilationRepository.findByPinned(pinned, pageable);
        }

        Collection<Long> compilationIds = compilationsLazy.stream()
                .map(Compilation::getId)
                .toList();

        Collection<Compilation> compilations = compilationRepository.findByIdIn(compilationIds);

        return compilations.stream()
                .map(compilation -> CompilationMapper.mapCompilationToCompilationDto(
                        compilation,
                        eventEnricher.toShortEventDtos(compilation.getEvents())
                ))
                .toList();
    }

    public CompilationDto getCompilationById(Long compilationId) {

        log.info("CompilationService: получение компиляции событий по id (compilationId = {})", compilationId);

        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Compilation with id (compilationId = %d) was not found",
                        compilationId
                ))
        );

        return CompilationMapper.mapCompilationToCompilationDto(
                compilation,
                eventEnricher.toShortEventDtos(compilation.getEvents())
        );
    }

}
