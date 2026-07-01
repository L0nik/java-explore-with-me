package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPost;
import ru.practicum.ewm.events.dto.LocationDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.users.User;
import ru.practicum.ewm.users.UserMapper;
import ru.practicum.ewm.users.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Collection<EventDto> getEventsOfUser(Long userId) {
        return List.of();
    }

    public EventDto createEventOfUser(Long userId, EventDtoPost eventData) {

        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId))
        );

        Category category = categoryRepository.findById(eventData.getCategory()).orElseThrow(
                () -> new NotFoundException(String.format("Категория с id = %d не найдена", eventData.getCategory()))
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
        return new EventDto();
    }
    public EventDto patchEventOfUser(Long userId, Long eventId) {
        return new EventDto();
    }


}
