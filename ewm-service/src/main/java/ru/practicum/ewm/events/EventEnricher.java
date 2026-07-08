package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoShort;
import ru.practicum.ewm.request.ConfirmedRequestsCount;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.RequestStatus;
import ru.practicum.ewm.stats.client.StatsClient;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventEnricher {

    private final StatsClient statsClient;
    private final RequestRepository requestRepository;

    public Collection<EventDto> toEventDtos(Collection<Event> events) {
        Map<String, Integer> views = getViewsForEvents(events);
        Map<Long, Integer> confirmedRequestsCounts = getConfirmedRequestsCounts(events);
        return events.stream()
                .map(event ->
                        EventMapper.mapEventToEventDto(
                                event,
                                findViewsForEvent(views, event),
                                confirmedRequestsCounts.getOrDefault(event.getId(), 0)
                        )
                )
                .toList();
    }

    public Set<EventDtoShort> toShortEventDtos(Set<Event> events) {
        Map<String, Integer> views = getViewsForEvents(events);
        Map<Long, Integer> confirmedRequestsCounts = getConfirmedRequestsCounts(events);
        return events.stream()
                .map(event ->
                        EventMapper.mapEventToEventDtoShort(
                                event,
                                findViewsForEvent(views, event),
                                confirmedRequestsCounts.getOrDefault(event.getId(), 0)
                        )
                )
                .collect(Collectors.toSet());
    }

    public EventDto toEventDto(Event event) {
        return EventMapper.mapEventToEventDto(event, getViewsForEvent(event), getConfirmedRequestsForEventCount(event));
    }

    private int findViewsForEvent(Map<String, Integer> views, Event event) {
        return views.getOrDefault("/events/" + event.getId(), 0);
    }

    private int getViewsForEvent(Event event) {
        Collection<String> uris = List.of("/events/" + event.getId());
        return findViewsForEvent(statsClient.getViews(uris), event);
    }

    private Map<String, Integer> getViewsForEvents(Collection<Event> events) {
        Collection<String> uris = events.stream()
                .map((event) -> "/events/" + event.getId()).toList();
        return statsClient.getViews(uris);
    }

    private int getConfirmedRequestsForEventCount(Event event) {
        return requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
    }

    private Map<Long, Integer> getConfirmedRequestsCounts(Collection<Event> events) {
        Map<Long, Integer> result = new HashMap<>();
        Collection<ConfirmedRequestsCount> confirmedRequestCounts = requestRepository.getRequestsCountByStatus(
                RequestStatus.CONFIRMED,
                events.stream().map(Event::getId).toList()
        );
        confirmedRequestCounts.forEach(confirmedRequestsCount ->
                result.put(confirmedRequestsCount.getEventId(), confirmedRequestsCount.getCount())
        );
        return result;
    }

}
