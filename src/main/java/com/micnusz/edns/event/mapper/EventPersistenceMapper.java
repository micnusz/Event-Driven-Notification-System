package com.micnusz.edns.event.mapper;

import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.event.dto.EventEnvelope;
import org.springframework.stereotype.Component;

@Component
public class EventPersistenceMapper {

    public EventEntity toEntity(EventEnvelope envelope) {
        return EventEntity.builder()
                .id(envelope.eventId())
                .type(envelope.type())
                .recipientId(envelope.recipientId())
                .payload(envelope.payload())
                .occurredAt(envelope.occurredAt())
                .build();
    }
}
