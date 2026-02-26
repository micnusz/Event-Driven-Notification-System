package com.micnusz.edns.notification.mapper;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.notification.dto.NotificationCommand;
import com.micnusz.edns.notification.dto.NotificationResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class NotificationMapper {

    public NotificationCommand toCommand(EventEnvelope envelope) {
        return new NotificationCommand(
                envelope.eventId(),
                envelope.type(),
                envelope.recipientId(),
                envelope.payload()
        );
    }

    public NotificationResponse toResponse(EventEnvelope event, String title, String message) {
        return new NotificationResponse(
                event.eventId(),
                event.type().name(),
                title,
                message,
                event.occurredAt(),
                event.payload()
        );
    }

    public NotificationResponse toResponse(NotificationCommand command, String title, String message) {
        return new NotificationResponse(
                command.eventId(),
                command.type().name(),
                title,
                message,
                Instant.now(),
                command.payload()
        );
    }
}