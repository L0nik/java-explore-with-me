package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    Collection<Request> findByRequesterId(Long requesterId);

    int countByEventIdAndStatus(Long eventId, RequestStatus status);

}
