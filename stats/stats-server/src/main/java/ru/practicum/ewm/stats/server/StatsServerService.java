package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.HitCreateDto;
import ru.practicum.ewm.stats.dto.StatsResponseDto;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatsServerService {

    private final StatsServerRepository statsRepository;

    @Transactional
    public void createHit(HitCreateDto HitData) {
        Hit hit = HitMapper.mapHitCreateDtoToHit(HitData);
        statsRepository.save(hit);
    }

    public Collection<StatsResponseDto> getStats() {
        return List.of();
    }
}
