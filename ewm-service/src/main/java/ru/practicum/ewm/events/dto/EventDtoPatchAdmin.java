package ru.practicum.ewm.events.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.ewm.moderation.dto.ModerationCommentDtoCreate;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventDtoPatchAdmin extends EventDtoPatch {
    Collection<ModerationCommentDtoCreate> moderationComments;
}
