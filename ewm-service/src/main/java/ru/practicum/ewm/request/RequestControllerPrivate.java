package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDto;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestControllerPrivate {

    private final RequestService requestService;

    @GetMapping
    public Collection<RequestDto> getRequests(@PathVariable Long userId) {
        log.info("RequestControllerPrivate: получение запросов на участие текущего пользователя (userId = {})", userId);
        return requestService.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("RequestControllerPrivate: создание запроса на участие (userId = {}, eventId = {})", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("RequestControllerPrivate: отмена своего запроса на участие в событии");
        return requestService.cancelRequest(userId, requestId);
    }

}
