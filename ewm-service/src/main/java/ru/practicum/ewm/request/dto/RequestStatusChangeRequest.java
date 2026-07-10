package ru.practicum.ewm.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.ewm.request.RequestStatus;

import java.util.Collection;

@Data
public class RequestStatusChangeRequest {

    @NotNull
    private Collection<Long> requestIds;

    @NotNull
    private RequestStatus status;
}
