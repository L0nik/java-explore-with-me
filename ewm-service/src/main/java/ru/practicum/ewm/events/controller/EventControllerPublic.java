package ru.practicum.ewm.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.events.EventService;
import ru.practicum.ewm.events.dto.EventDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPublic {

    private final EventService eventService;

    @GetMapping
    public Collection<EventDto> getEvents() {
        log.info("EventControllerPublic: получение списка событий");
        return List.of();
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId) {
        log.info("EventControllerPublic: получение события (eventId = {})", eventId);
        return new EventDto();
    }

}
