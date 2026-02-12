package com.micnusz.edns.notification;

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
                envelope.getEventId(),
                envelope.getType(),
                envelope.getRecipientId(),
                envelope.getPayload()
        );
    }
}
