package com.micnusz.edns.event.dto;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEnvelope {
        private UUID eventId;
        private EventType type;
        private String recipientId;
        private Instant occurredAt;
        private EventPayload payload;
        private int version;
}