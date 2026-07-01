package ru.practicum.ewm.events.dto;

import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.events.EventState;
import ru.practicum.ewm.users.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
    private UserDto initiator;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private EventState state;
    private int confirmedRequests;
    private long views;
}
