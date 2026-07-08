package ru.practicum.ewm.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationDtoPatch;
import ru.practicum.ewm.compilation.dto.CompilationDtoPost;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid CompilationDtoPost compilationData) {
        log.info(
                "CompilationControllerAdmin: создание компиляции событий администратором (compilationData = {})",
                compilationData
        );
        return compilationService.createCompilation(compilationData);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compilationId) {
        log.info(
                "CompilationControllerAdmin: удаление компиляции событий администратором (compilationId = {})",
                compilationId
        );
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto patchCompilation(
            @PathVariable Long compilationId,
            @RequestBody @Valid CompilationDtoPatch compilationData
    ) {
        log.info(
                """
                        CompilationControllerAdmin: изменение компиляции событий администратором
                        (compilationId = {}, compilationData = {})
                """,
                compilationId,
                compilationData
        );
        return compilationService.patchCompilation(compilationId, compilationData);
    }

}
