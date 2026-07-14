package ru.practicum.ewm.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.EventService;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPatch;
import ru.practicum.ewm.events.dto.EventDtoPost;
import ru.practicum.ewm.events.dto.EventDtoPrivate;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestStatusChangeRequest;
import ru.practicum.ewm.request.dto.RequestStatusChangeResponse;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {

    private final EventService eventService;

    @GetMapping
    public Collection<EventDtoPrivate> getEventsOfUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("EventControllerPrivate: получение событий пользователя (userId = {})", userId);
        return eventService.getEventsPrivate(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEventOfUser(@PathVariable Long userId, @RequestBody @Valid EventDtoPost eventData) {
        log.info(
                "EventControllerPrivate: создание события пользователем (userId = {}, eventData = {})",
                userId,
                eventData
        );
        return eventService.createEventPrivate(userId, eventData);
    }

    @GetMapping("/{eventId}")
    public EventDtoPrivate getEventOfUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventControllerPrivate: получение события пользователя (userId = {}, eventId = {})", userId, eventId);
        return eventService.getEventByIdPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto patchEventOfUser(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid EventDtoPatch eventData
    ) {
        log.info(
                "EventControllerPrivate: изменение события пользователем (userId = {}, eventId = {}, eventData = {})",
                userId,
                eventId,
                eventData
        );
        return eventService.patchEventPrivate(userId, eventId, eventData);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<RequestDto> getRequestsForEventOfUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info(
                """
                        EventControllerPrivate:
                        получение запросов на участие в событии текущего пользователя (userId = {}, eventId = {})
                """,
                userId,
                eventId
        );
        return eventService.getRequestsForEventPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusChangeResponse changeRequestsStatusesForEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid RequestStatusChangeRequest statusChangeRequest
    ) {
        log.info("""
                    EventControllerPrivate: изменение статуса заявок на участие в событии текущего пользователя
                    (userId = {}, eventId = {}, statusChangeRequest = {})
                """,
                userId,
                eventId,
                statusChangeRequest
        );
        return eventService.changeRequestsStatusesForEvent(userId, eventId, statusChangeRequest);
    }

}
