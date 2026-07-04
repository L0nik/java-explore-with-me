package ru.practicum.ewm.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.events.EventService;
import ru.practicum.ewm.events.EventSort;
import ru.practicum.ewm.events.dto.EventDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPublic {

    private final EventService eventService;

    @GetMapping
    public Collection<EventDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Collection<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        log.info("""
                EventControllerPublic: получение списка событий
                (text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {},
                onlyAvailable = {}, sort = {}, from = {}, size = {})
                """,
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.findEventsPublic(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request.getRemoteAddr(),
                request.getRequestURI()
        );
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("EventControllerPublic: получение события (eventId = {})", eventId);
        return eventService.getEventByIdPublic(eventId, request.getRemoteAddr(), request.getRequestURI());
    }

}
