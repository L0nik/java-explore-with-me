package ru.practicum.ewm.events.dto;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.ewm.moderation.dto.ModerationCommentDtoCreate;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventDtoPatchAdmin extends EventDtoPatch {
    @Valid
    Collection<ModerationCommentDtoCreate> moderationComments;
}
