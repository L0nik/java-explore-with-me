package ru.practicum.ewm.stats.server;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.HitCreateDto;
import ru.practicum.ewm.stats.dto.StatsResponseDto;

@UtilityClass
public class StatsMapper {

    public Hit mapHitCreateDtoToHit(HitCreateDto hitData) {
        Hit result = new Hit();
        result.setApp(hitData.getApp());
        result.setUri(hitData.getUri());
        result.setIp(hitData.getIp());
        result.setTimestamp(hitData.getTimestamp());
        return result;
    }

    public StatsResponseDto mapStatsViewToStatsResponseDto(StatsView statsView) {
        return new StatsResponseDto(
                statsView.getApp(),
                statsView.getUri(),
                statsView.getHits()
        );
    }

}
