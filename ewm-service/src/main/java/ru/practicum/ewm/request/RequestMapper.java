package ru.practicum.ewm.request;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.dto.RequestDto;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {

    public RequestDto mapRequestToRequestDto(Request request) {
        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setEvent(request.getEventId());
        dto.setRequester(request.getRequesterId());
        dto.setStatus(request.getStatus());
        dto.setCreated(request.getCreated());
        return dto;
    }
}
