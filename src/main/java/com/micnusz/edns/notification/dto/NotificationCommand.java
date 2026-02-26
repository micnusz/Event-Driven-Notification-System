package com.micnusz.edns.notification.dto;

import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.dto.EventEnvelope;

import java.util.UUID;

public record NotificationCommand(
        UUID eventId,
        EventType type,
        String recipientId,
        EventPayload payload
) {
    public static NotificationCommand from(EventEnvelope envelope) {
        return new NotificationCommand(
                envelope.eventId(),
                envelope.type(),
                envelope.recipientId(),
                envelope.payload()
        );
    }
}
