package com.micnusz.edns.dto;

import com.micnusz.edns.model.EventEnvelope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    String id;
    String type;
    String title;
    String message;
    String timestamp;
    Map<String, Object> data;

    public static NotificationResponse from(EventEnvelope event, String title, String message) {
        return NotificationResponse.builder()
                .id(event.getEventId().toString())
                .type(event.getType().name())
                .title(title)
                .message(message)
                .timestamp(event.getOccurredAt().toString())
                .data(event.getPayload())
                .build();
    }
}
