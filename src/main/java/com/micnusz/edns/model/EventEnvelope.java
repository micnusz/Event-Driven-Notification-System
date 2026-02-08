package com.micnusz.edns.model;

import com.micnusz.edns.enums.EventType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record EventEnvelope(
        UUID eventId,
        EventType type,
        String recipientId,
        Instant occurredAt,
        Map<String, Object> payload,
        int version
) {}
