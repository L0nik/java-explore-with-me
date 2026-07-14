package ru.practicum.ewm.events.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.ewm.moderation.dto.ModerationCommentDto;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventDtoPrivate extends EventDto {
    private Collection<ModerationCommentDto> moderationComments;
}
