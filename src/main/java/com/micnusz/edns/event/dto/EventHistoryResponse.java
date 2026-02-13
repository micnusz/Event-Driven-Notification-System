package com.micnusz.edns.event.dto;


import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.notification.builder.NotificationMessageBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventHistoryResponse {
    private UUID id;
    private EventType type;
    private String recipientId;
    private Instant timestamp;
    private String title;
    private String message;
    private EventPayload payload;

    public static EventHistoryResponse from(
            EventEntity entity,
            List<NotificationMessageBuilder> builders) {

        NotificationMessageBuilder builder = builders.stream()
                .filter(b -> b.supports() == entity.getType())
                .findFirst()
                .orElse(null);

        String title;
        String message;

        if (builder != null && entity.getPayload() != null) {
            try {
                title = builder.buildTitle(entity.getPayload());
                message = builder.buildMessage(entity.getPayload());
            } catch (Exception e) {
                title = "Event: " + entity.getType();
                message = "Failed to build message: " + e.getMessage();
            }
        } else {
            title = "Event: " + entity.getType();
            message = entity.getType().name();
        }

        return EventHistoryResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .recipientId(entity.getRecipientId())
                .timestamp(entity.getOccurredAt())
                .title(title)
                .message(message)
                .payload(entity.getPayload())
                .build();
    }
}