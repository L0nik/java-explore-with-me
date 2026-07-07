package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationDtoPatch;
import ru.practicum.ewm.compilation.dto.CompilationDtoPost;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    private final CompilationRepository compilationRepository;

    public CompilationDto createCompilation(CompilationDtoPost compilationData) {
        return new CompilationDto();
    }

    public void deleteCompilation(Long compilationId) {
        //
    }

    public CompilationDto patchCompilation(Long compilationId, CompilationDtoPatch compilationData) {
        return new CompilationDto();
    }

    public Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return List.of();
    }

    public CompilationDto getCompilationById(Long compilationId) {
        return new CompilationDto();
    }

}
