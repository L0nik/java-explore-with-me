package ru.practicum.ewm.moderation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.events.Event;
import ru.practicum.ewm.moderation.dto.ModerationCommentDtoCreate;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ModerationCommentService {

    private final ModerationCommentRepository moderationCommentRepository;

    @Transactional
    public void addComments(Event event, Collection<ModerationCommentDtoCreate> commentDtos) {

        log.info("ModerationCommentService: добавление комментариев администратора для события (event = {}, commentDtos = {})",
                event,
                commentDtos
        );

        Collection<ModerationComment> moderationComments = commentDtos.stream()
                .map((comment) ->
                        ModerationCommentMapper.mapModerationCommentDtoCreateToEntity(
                                comment,
                                event.getId(),
                                LocalDateTime.now(),
                                event.getRevisionNumber()
                        ))
                .toList();
        moderationCommentRepository.saveAll(moderationComments);

    }

}
