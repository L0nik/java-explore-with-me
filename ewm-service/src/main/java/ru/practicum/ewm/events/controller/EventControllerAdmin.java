package ru.practicum.ewm.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.EventService;
import ru.practicum.ewm.events.EventState;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPatch;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    public Collection<EventDto> getEvents(
            @RequestParam(required = false) Collection<Long> users,
            @RequestParam(required = false) Collection<EventState> states,
            @RequestParam(required = false) Collection<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("EventControllerAdmin: получение списка событий");
        return eventService.findEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDto patchEvent(@PathVariable Long eventId, @RequestBody EventDtoPatch eventData) {
        log.info("EventControllerAdmin: изменение данных события (eventId = {}, eventData = {})", eventId, eventData);
        return eventService.patchEventAdmin(eventId, eventData);
    }

}
