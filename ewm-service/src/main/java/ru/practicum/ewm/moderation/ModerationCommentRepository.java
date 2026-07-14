package ru.practicum.ewm.moderation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ModerationCommentRepository extends JpaRepository<ModerationComment, Long> {

    Collection<ModerationComment> findByEventId(Long eventId);

    Collection<ModerationComment> findByEventIdIn(Collection<Long> eventId);

}
