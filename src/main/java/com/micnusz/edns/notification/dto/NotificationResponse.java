package com.micnusz.edns.notification.dto;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.notification.NotificationCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private UUID id;
    private String type;
    private String title;
    private String message;
    private Instant timestamp;
    private EventPayload payload;

    public static NotificationResponse from(EventEnvelope event, String title, String message) {
        return NotificationResponse.builder()
                .id(event.getEventId())
                .type(event.getType().name())
                .title(title)
                .message(message)
                .timestamp(event.getOccurredAt())
                .payload(event.getPayload())
                .build();
    }

    public static NotificationResponse from(NotificationCommand command, String title, String message) {
        return NotificationResponse.builder()
                .id(command.eventId())
                .type(command.type().name())
                .title(title)
                .message(message)
                .timestamp(Instant.now())
                .payload(command.payload())
                .build();
    }
}
