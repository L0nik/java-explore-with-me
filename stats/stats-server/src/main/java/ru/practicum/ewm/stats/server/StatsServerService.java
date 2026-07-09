package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.stats.dto.HitCreateDto;
import ru.practicum.ewm.stats.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatsServerService {

    private final StatsServerRepository statsRepository;

    @Transactional
    public void createHit(HitCreateDto hitData) {
        log.info("StatsServerService: создание hit {}", hitData);
        Hit hit = StatsMapper.mapHitCreateDtoToHit(hitData);
        statsRepository.save(hit);
        log.info("StatsServerService: hit успешно создан {}", hitData);
    }

    public Collection<StatsResponseDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris,
            Boolean unique
    ) {
        log.info("StatsServerService: получение статистики");
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "start date must be before end date");
        }
        Collection<StatsView> stats = statsRepository.getStats(start, end, uris, unique);
        log.info("StatsServerService: статистика получена {}", stats);
        return stats.stream()
                .map(StatsMapper::mapStatsViewToStatsResponseDto)
                .toList();
    }
}
