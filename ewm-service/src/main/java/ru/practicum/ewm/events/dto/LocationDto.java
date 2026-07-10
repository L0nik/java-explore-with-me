package ru.practicum.ewm.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDto {
    private Double lat;
    private Double lon;
}
