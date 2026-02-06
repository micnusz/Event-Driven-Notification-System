package com.micnusz.edns.dto;

import com.micnusz.edns.enums.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EventResponse {
    private Long id;
    private EventType type;
    private String payload;
    private Long recipientId;
    private Long actorId;
    private LocalDateTime createdAt;
}
