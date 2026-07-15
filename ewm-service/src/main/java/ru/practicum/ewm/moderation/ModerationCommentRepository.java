package ru.practicum.ewm.moderation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ModerationCommentRepository extends JpaRepository<ModerationComment, Long> {

    Collection<ModerationComment> findByEventIdAndRevisionNumber(Long eventId, int revisionNumber);

    @Query("""
            SELECT mc
            FROM ModerationComment mc
            JOIN Event e
            ON mc.eventId = e.id AND mc.revisionNumber = e.revisionNumber
            WHERE mc.eventId IN (:eventIds)
            """)
    Collection<ModerationComment> getCommentsForEvents(Collection<Long> eventIds);

}
