package ru.practicum.ewm.moderation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModerationCommentDtoCreate {
    @NotBlank
    private String content;
}
