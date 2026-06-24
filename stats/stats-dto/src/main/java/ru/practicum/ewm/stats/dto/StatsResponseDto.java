package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponseDto {
    String app;
    String uri;
    Integer hits;
}
