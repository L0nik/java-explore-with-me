package ru.practicum.ewm.request.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class RequestStatusChangeResponse {
    private Collection<RequestDto> confirmedRequests = new ArrayList<>();
    private Collection<RequestDto> rejectedRequests = new ArrayList<>();
}
