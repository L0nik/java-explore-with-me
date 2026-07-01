package ru.practicum.ewm.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.EventService;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPost;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {

    private final EventService eventService;

    @GetMapping
    public Collection<EventDto> getEventsOfUser(@PathVariable Long userId) {
        log.info("EventControllerPrivate: получение событий пользователя (userId = {})", userId);
        return eventService.getEventsOfUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEventOfUser(@PathVariable Long userId, @RequestBody @Valid EventDtoPost eventData) {
        log.info(
                "EventControllerPrivate: создание события пользователем (userId = {}, eventData = {})",
                userId,
                eventData
        );
        return eventService.createEventOfUser(userId, eventData);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventOfUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventControllerPrivate: получение события пользователя (userId = {}, eventId = {})", userId, eventId);
        return eventService.getEventOfUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto patchEventOfUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("EventControllerPrivate: изменение события пользователем (userId = {}, eventId = {})", userId, eventId);
        return eventService.patchEventOfUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public void getRequestsForEventOfUser(@PathVariable Long userId, @PathVariable Long eventId) {
        // TODO: сделать после реализации запросов на участие в событиях (requests)
    }

    @PatchMapping("/{eventId}/requests")
    public void changeStatusOfRequestsForEvent() {
        // TODO: сделать после реализации запросов на участие в событиях (requests)
    }

}
