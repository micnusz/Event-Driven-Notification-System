package com.micnusz.edns.notification.dto;

import com.micnusz.edns.event.payload.EventPayload;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String type,
        String title,
        String message,
        Instant timestamp,
        EventPayload payload
) {}