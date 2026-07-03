package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.events.EventState;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.dto.UserDtoShort;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
    private UserDtoShort initiator;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private EventState state;
    private int confirmedRequests;
    private long views;
}
