package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitCreateDto;
import ru.practicum.ewm.stats.dto.StatsResponseDto;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsServerController {

    private final StatsServerService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody HitCreateDto hitData) {
        log.info("StatsServerController: сохранение информации о том, что на uri сервиса был отправлен запрос");
        statsService.createHit(hitData);
    }

    @GetMapping("/stats")
    public Collection<StatsResponseDto> getStats() {
        log.info("StatsServerController: получение статистики по посещениям");
        return statsService.getStats();
    }
}
