package ru.practicum.ewm.events;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collection;

@UtilityClass
public class EventSpecification {

    public Specification<Event> initiatorIn(Collection<Long> users) {
        return (root, query, cb) ->
                root.get("initiator").get("id").in(users);
    }

    public Specification<Event> stateIn(Collection<EventState> states) {
        return (root, query, cb) ->
                root.get("state").in(states);
    }

    public Specification<Event> categoryIn(Collection<Long> categories) {
        return (root, query, cb) ->
                root.get("category").get("id").in(categories);
    }

    public Specification<Event> eventDateAfter(LocalDateTime rangeStart) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
    }

    public Specification<Event> eventDateBefore(LocalDateTime rangeEnd) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
    }

}
