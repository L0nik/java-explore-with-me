package ru.practicum.ewm.moderation.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModerationCommentDto {
    private Long id;
    private Long eventId;
    private String content;
    private LocalDateTime created;
    private int revisionNumber;
}
