package com.micnusz.edns.event.mapper;

import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.model.EventEnvelope;
import org.springframework.stereotype.Component;

@Component
public class EventPersistenceMapper {

    public EventEntity toEntity(EventEnvelope envelope) {
        return EventEntity.builder()
                .id(envelope.getEventId())
                .type(envelope.getType().name())
                .recipientId(envelope.getRecipientId())
                .payload(envelope.getPayload())
                .occurredAt(envelope.getOccurredAt())
                .build();
    }
}
