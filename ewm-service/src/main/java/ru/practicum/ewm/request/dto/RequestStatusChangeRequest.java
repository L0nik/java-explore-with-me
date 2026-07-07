package ru.practicum.ewm.request.dto;

import lombok.Data;
import ru.practicum.ewm.request.RequestStatus;

import java.util.Collection;

@Data
public class RequestStatusChangeRequest {
    private Collection<Long> requestIds;
    private RequestStatus status;
}
