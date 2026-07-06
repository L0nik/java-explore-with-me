package ru.practicum.ewm.request.dto;

import lombok.Data;
import ru.practicum.ewm.request.RequestStatus;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private RequestStatus status;
    private LocalDateTime created;
}
