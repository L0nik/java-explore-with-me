package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(
            @RequestParam Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("""
                CompilationControllerPublic: получение списка компиляций событий
                (pinned = {}, from = {}, size = {})
                """,
                pinned,
                from,
                size
        );
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilationId")
    public CompilationDto getCompilationById(@PathVariable Long compilationId) {
        log.info("CompilationControllerPublic: получение компиляции событий по id (compilationId = {})", compilationId);
        return compilationService.getCompilationById(compilationId);
    }
}
