package com.micnusz.edns.dto;

import java.time.Instant;
import java.util.UUID;

public record EventResponse(
        UUID eventId,
        Instant occurredAt
) {}