package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.events.Event;
import ru.practicum.ewm.events.EventRepository;
import ru.practicum.ewm.events.EventState;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.users.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public Collection<RequestDto> getRequests(Long userId) {

        log.info("RequestService: получение списка своих запросов на участие (userId = {})", userId);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id = %d not found", userId));
        }

        Collection<Request> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(RequestMapper::mapRequestToRequestDto)
                .toList();

    }

    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {

        log.info("RequestService: создание запроса на участие (userId = {}, eventId = {})", userId, eventId);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id = %d not found", userId));
        }

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            String message = String.format(
                    "Request for event (eventId = %d) user (userId = %d) already exists",
                    eventId,
                    userId
            );
            throw new ConflictException(message);
        };

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id = %d not found", eventId))
        );

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The event initiator cannot participate in their own event.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("User cannot participate in an unpublished event");
        }

        int confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() <= confirmedRequests) {
            throw new ConflictException("The participant limit has been reached");
        }

        Request request = new Request();
        request.setEventId(eventId);
        request.setRequesterId(userId);
        request.setStatus(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);
        request.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));

        requestRepository.save(request);

        return RequestMapper.mapRequestToRequestDto(request);
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        log.info(
                "RequestService: отмена своего запроса на участие в событии (userId = {}, requestId = {})",
                userId,
                requestId
        );

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id = %d not found", userId));
        }

        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format("Request with id=%d was not found", requestId))
        );

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return RequestMapper.mapRequestToRequestDto(request);
    }

}
