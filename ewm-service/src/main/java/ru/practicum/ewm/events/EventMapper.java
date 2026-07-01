package ru.practicum.ewm.events;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.events.dto.EventDto;
import ru.practicum.ewm.events.dto.EventDtoPost;
import ru.practicum.ewm.users.User;

@UtilityClass
public class EventMapper {

    public EventDto mapEventToEventDto(Event event) {
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
        dto.setViews(event.getViews());
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

}
