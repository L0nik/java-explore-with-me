package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    Collection<Request> findByRequesterId(Long requesterId);

    int countByEventIdAndStatus(Long eventId, RequestStatus status);

    Collection<Request> findByEventId(Long eventId);

    Collection<Request> findByEventIdAndIdIn(Long eventId, Collection<Long> requestIds);

    @Query("""
            SELECT r.eventId as eventId, COUNT(r.id) as count
            FROM Request r
            WHERE r.status = :status AND r.eventId IN :eventIds
            GROUP BY r.eventId
            """)
    Collection<ConfirmedRequestsCount> getRequestsCountByStatus(RequestStatus status, Collection<Long> eventIds);

}
