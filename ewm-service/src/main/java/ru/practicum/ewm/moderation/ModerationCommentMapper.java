package ru.practicum.ewm.moderation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.moderation.dto.ModerationCommentDto;
import ru.practicum.ewm.moderation.dto.ModerationCommentDtoCreate;

import java.time.LocalDateTime;

@UtilityClass
public class ModerationCommentMapper {

    public ModerationComment mapModerationCommentDtoCreateToEntity(
            ModerationCommentDtoCreate dto,
            Long eventId,
            LocalDateTime created,
            int revisionNumber
    ) {
        ModerationComment moderationComment = new ModerationComment();
        moderationComment.setContent(dto.getContent());
        moderationComment.setEventId(eventId);
        moderationComment.setCreated(created);
        moderationComment.setRevisionNumber(revisionNumber);
        return moderationComment;
    }

    public ModerationCommentDto mapEntityToModerationCommentDto(ModerationComment comment) {
        ModerationCommentDto dto = new ModerationCommentDto();
        dto.setId(comment.getId());
        dto.setEventId(comment.getEventId());
        dto.setContent(comment.getContent());
        dto.setCreated(comment.getCreated());
        dto.setRevisionNumber(comment.getRevisionNumber());
        return dto;
    }

}
