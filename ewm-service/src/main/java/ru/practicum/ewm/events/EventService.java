package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPatch;
import ru.practicum.ewm.events.dto.EventDtoPost;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.HitCreateDto;
import ru.practicum.ewm.stats.dto.StatsResponseDto;
import ru.practicum.ewm.users.User;
import ru.practicum.ewm.users.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final StatsClient statsClient;

    public Collection<EventDto> getEventsPrivate(Long userId, int from, int size) {
        log.info("EventService: получение событий пользователя (userId = {}, from = {}, size = {})", userId, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        Map<String, Integer> views = getViewsForEvents(events);
        return events.stream()
                .map(event -> EventMapper.mapEventToEventDto(event, findViewsForEvent(views, event)))
                .toList();
    }

    @Transactional
    public EventDto createEventPrivate(Long userId, EventDtoPost eventData) {

        log.info("EventService: создание события пользователем (userId = {}, eventData = {})", userId, eventData);

        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id = %d not found", userId))
        );

        Category category = categoryRepository.findById(eventData.getCategory()).orElseThrow(
                () -> new NotFoundException(String.format("Category with id = %d not found", eventData.getCategory()))
        );

        if(eventData.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            String message = String.format(
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                    eventData.getEventDate()
            );
            throw new ValidationException(message);
        }

        Event event = EventMapper.mapEventDtoPostToEvent(eventData);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        eventRepository.save(event);

        return EventMapper.mapEventToEventDto(event, getViewsForEvent(event));

    }

    public EventDto getEventByIdPrivate(Long userId, Long eventId) {

        log.info("EventService: получения события пользователя (userId = {}, eventId = {})", userId, eventId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id = %d not found", userId))
        );

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id = %d not found", eventId))
        );

        return EventMapper.mapEventToEventDto(event, getViewsForEvent(event));
    }

    @Transactional
    public EventDto patchEventPrivate(Long userId, Long eventId, EventDtoPatch eventData) {

        log.info(
                "EventService: изменение события пользователем (userId = {}, eventId = {}, eventData = {})",
                userId,
                eventId,
                eventData
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id = %d not found", userId))
        );

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id = %d not found", eventId))
        );

        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new ValidationException("Event must not be published");
        }

        if (eventData.getCategory() != null) {
            Category category = categoryRepository.findById(eventData.getCategory()).orElseThrow(
                    () -> new NotFoundException(String.format("Category with id = %d not found", eventData.getCategory()))
            );
            event.setCategory(category);
        }

        EventMapper.updateEvent(event, eventData, false);

        eventRepository.save(event);

        return EventMapper.mapEventToEventDto(event, getViewsForEvent(event));
    }

    @Transactional
    public EventDto patchEventAdmin(Long eventId, EventDtoPatch eventData) {

        log.info(
                "EventService: изменение события администратором (eventId = {}, eventData = {})",
                eventId,
                eventData
        );

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id = %d not found", eventId))
        );

        if (eventData.getCategory() != null) {
            Category category = categoryRepository.findById(eventData.getCategory()).orElseThrow(
                    () -> new NotFoundException(String.format("Category with id = %d not found", eventData.getCategory()))
            );
            event.setCategory(category);
        }

        EventMapper.updateEvent(event, eventData, true);

        eventRepository.save(event);

        return EventMapper.mapEventToEventDto(event, getViewsForEvent(event));
    }

    public Collection<EventDto> findEventsAdmin(
            Collection<Long> users,
            Collection<EventState> states,
            Collection<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size
    ) {
        log.info(
                """
                        EventService: получение списка событий администратором (
                        users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd ={}, from = {}, size = {})
                """,
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size
        );


        Pageable pageable = PageRequest.of(from / size, size);

        Specification<Event> spec = Specification.where(EventSpecification.withFetch());

        if (users != null) {
            spec = spec.and(EventSpecification.initiatorIn(users));
        }

        if (states != null) {
            spec = spec.and(EventSpecification.stateIn(states));
        }

        if (categories != null) {
            spec = spec.and(EventSpecification.categoryIn(categories));
        }

        if (rangeStart != null) {
            spec = spec.and(EventSpecification.eventDateAfter(rangeStart));
        }

        if (rangeEnd != null) {
            spec = spec.and(EventSpecification.eventDateBefore(rangeEnd));
        }

        Collection<Event> events = eventRepository.findAll(spec, pageable).toList();
        Map<String, Integer> views = getViewsForEvents(events);

        return events
                .stream()
                .map(event -> {
                    return EventMapper.mapEventToEventDto(event, findViewsForEvent(views, event));
                })
                .toList();
    }

    public EventDto getEventByIdPublic(Long eventId, String ip, String uri) {

        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException(String.format("Event with id = %d not found", eventId))
        );

        saveHit(ip, uri);

        return EventMapper.mapEventToEventDto(event, getViewsForEvent(event));
    }

    public Collection<EventDto> findEventsPublic(
            String text,
            Collection<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort,
            int from,
            int size,
            String ip,
            String uri
    ) {

        saveHit(ip, uri);

        Sort sortBy = Sort.by("eventDate").ascending();

        Specification<Event> spec = Specification.where(EventSpecification.withFetch())
                .and(EventSpecification.stateIn(List.of(EventState.PUBLISHED)));

        if (text != null) {
            spec = spec.and(EventSpecification.containsText(text));
        }

        if (categories != null) {
            spec = spec.and(EventSpecification.categoryIn(categories));
        }

        if (paid != null) {
            spec = spec.and(EventSpecification.isPaid(paid));
        }

        if (rangeStart != null) {
            spec = spec.and(EventSpecification.eventDateAfter(rangeStart));
        }

        if (rangeEnd != null) {
            spec = spec.and(EventSpecification.eventDateBefore(rangeEnd));
        }

        if (rangeStart == null && rangeEnd == null) {
            spec = spec.and(EventSpecification.eventDateAfter(LocalDateTime.now()));
        }

        if (onlyAvailable != null && onlyAvailable.equals(true)) {
            spec = spec.and(EventSpecification.isAvailable());
        }

        Collection<Event> events = eventRepository.findAll(spec, sortBy);
        Map<String, Integer> views = getViewsForEvents(events);

        List<EventDto> result = events.stream()
                .map(event -> EventMapper.mapEventToEventDto(event, findViewsForEvent(views, event)))
                .collect(Collectors.toList());

        if (sort != null && sort.equals(EventSort.VIEWS)) {
            result.sort(Comparator.comparing(EventDto::getViews).reversed());
        }

        return result.stream().skip(from).limit(size).toList();
    }

    private void saveHit(String ip, String uri) {
        HitCreateDto hitCreateDto = new HitCreateDto();
        hitCreateDto.setApp("ewm-service");
        hitCreateDto.setUri(uri);
        hitCreateDto.setIp(ip);
        hitCreateDto.setTimestamp(LocalDateTime.now());
        statsClient.postHit(hitCreateDto);
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

}
