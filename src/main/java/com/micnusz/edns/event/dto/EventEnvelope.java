package com.micnusz.edns.event.dto;

import com.micnusz.edns.event.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEnvelope {
        UUID eventId;
        EventType type;
        String recipientId;
        Instant occurredAt;
        EventPayload payload;
        int version;
}