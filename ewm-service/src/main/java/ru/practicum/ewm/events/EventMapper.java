package ru.practicum.ewm.events;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPatch;
import ru.practicum.ewm.events.dto.EventDtoPost;
import ru.practicum.ewm.events.dto.LocationDto;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.users.UserMapper;

import java.time.LocalDateTime;
import java.util.Collection;

@UtilityClass
public class EventMapper {

    public EventDto mapEventToEventDto(Event event, int views) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setPaid(event.isPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setRequestModeration(event.isRequestModeration());
        dto.setTitle(event.getTitle());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setState(event.getState());
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
        dto.setInitiator(UserMapper.mapUserToUserDtoShort(event.getInitiator()));
        dto.setCategory(CategoryMapper.mapCategoryToCategoryDto(event.getCategory()));
        dto.setViews(views);
        return dto;
    }

    public Event mapEventDtoPostToEvent(EventDtoPost dto) {

        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setPaid(dto.isPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.isRequestModeration());
        event.setTitle(dto.getTitle());

        Location location = new Location(dto.getLocation().getLat(), dto.getLocation().getLon());
        event.setLocation(location);

        return event;

    }

    public void updateEvent(Event event, EventDtoPatch dto, boolean byAdmin) {

        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {
            event.setAnnotation(dto.getAnnotation());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            event.setDescription(dto.getDescription());
        }

        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                String message = String.format(
                        "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                        dto.getEventDate()
                );
                throw new ValidationException(message);
            } else {
                event.setEventDate(dto.getEventDate());
            }
        }

        if (dto.getLocation() != null) {
            event.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        }

        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }

        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }

        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }

        StateAction stateAction = dto.getStateAction();
        if (stateAction != null) {
            if (stateAction.equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (stateAction.equals(StateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else if (byAdmin && stateAction.equals(StateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(EventState.PENDING)) {
                    throw new ValidationException(
                            "Cannot publish the event because it's not in the right state: " + event.getState()
                    );
                }
                event.setState(EventState.PUBLISHED);
            } else if (byAdmin && stateAction.equals(StateAction.REJECT_EVENT)) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ValidationException(
                            "Cannot reject the event because it's not in the right state: " + event.getState()
                    );
                }
                event.setState(EventState.CANCELED);
            }
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            event.setTitle(dto.getTitle());
        }
    }

}
