package com.micnusz.edns.dto;

import com.micnusz.edns.model.EventEnvelope;

import java.util.Map;

public record NotificationResponse(
        String id,
        String type,
        String title,
        String message,
        String timestamp,
        Map<String, Object> data
) {
    public static NotificationResponse from(EventEnvelope event, String title, String message) {
        return new NotificationResponse(
                event.eventId().toString(),
                event.type().toString(),
                title,
                message,
                event.occurredAt(),
                event.payload()
        );
    }
}
