package com.micnusz.edns.event.dto;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope(
        UUID eventId,
        EventType type,
        String recipientId,
        Instant occurredAt,
        EventPayload payload,
        int version
) {}