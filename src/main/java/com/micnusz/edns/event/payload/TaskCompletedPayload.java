package com.micnusz.edns.event.payload;

import java.time.Instant;

public record TaskCompletedPayload (
         String taskName,
         String completedBy,
         Instant completedAt
) implements EventPayload {
}