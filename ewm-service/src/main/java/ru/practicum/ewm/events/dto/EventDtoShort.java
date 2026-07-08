package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.users.dto.UserDtoShort;

import java.time.LocalDateTime;

@Data
public class EventDtoShort {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserDtoShort initiator;
    private boolean paid;
    private String title;
    private long views;

}
