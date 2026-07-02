package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPatch;
import ru.practicum.ewm.events.dto.EventDtoPost;
import ru.practicum.ewm.events.dto.LocationDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.users.User;
import ru.practicum.ewm.users.UserMapper;
import ru.practicum.ewm.users.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Collection<EventDto> getEventsOfUser(Long userId, int from, int size) {
        log.info("EventService: получение событий пользователя (userId = {}, from = {}, size = {})", userId, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findByInitiatorId(userId, pageable).stream()
                .map((event) -> {
                    EventDto eventDto = EventMapper.mapEventToEventDto(event);
                    eventDto.setCategory(CategoryMapper.mapCategoryToCategoryDto(event.getCategory()));
                    eventDto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
                    eventDto.setInitiator(UserMapper.mapUserToUserDtoShort(event.getInitiator()));
                    return eventDto;
                })
                .toList();
    }

    @Transactional
    public EventDto createEventOfUser(Long userId, EventDtoPost eventData) {

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

        EventDto eventDto = EventMapper.mapEventToEventDto(event);
        eventDto.setCategory(CategoryMapper.mapCategoryToCategoryDto(category));
        eventDto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
        eventDto.setInitiator(UserMapper.mapUserToUserDtoShort(initiator));
        return eventDto;

    }

    public EventDto getEventOfUser(Long userId, Long eventId) {

        log.info("EventService: получения события пользователя (userId = {}, eventId = {})", userId, eventId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id = %d not found", userId))
        );

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id = %d not found", eventId))
        );

        EventDto eventDto = EventMapper.mapEventToEventDto(event);
        eventDto.setCategory(CategoryMapper.mapCategoryToCategoryDto(event.getCategory()));
        eventDto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
        eventDto.setInitiator(UserMapper.mapUserToUserDtoShort(user));
        return eventDto;
    }

    @Transactional
    public EventDto patchEventOfUser(Long userId, Long eventId, EventDtoPatch eventData) {

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

        EventMapper.updateEvent(event, eventData);

        eventRepository.save(event);
        EventDto eventDto = EventMapper.mapEventToEventDto(event);
        eventDto.setCategory(CategoryMapper.mapCategoryToCategoryDto(event.getCategory()));
        eventDto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
        eventDto.setInitiator(UserMapper.mapUserToUserDtoShort(user));
        return eventDto;
    }

}
