package ru.practicum.ewm.moderation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModerationCommentDtoCreate {
    @NotBlank
    private String content;
}
