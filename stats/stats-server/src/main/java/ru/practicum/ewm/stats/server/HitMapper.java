package ru.practicum.ewm.stats.server;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.HitCreateDto;

import java.time.LocalDateTime;

@UtilityClass
public class HitMapper {

    public Hit mapHitCreateDtoToHit(HitCreateDto hitData) {
        Hit result = new Hit();
        result.setApp(hitData.getApp());
        result.setUri(hitData.getUri());
        result.setIp(hitData.getIp());
        result.setTimestamp(hitData.getTimestamp());
        return result;
    }

}
