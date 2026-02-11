package com.micnusz.edns.service;


import com.micnusz.edns.dto.EventRequest;
import com.micnusz.edns.dto.EventResponse;
import com.micnusz.edns.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    public EventResponse createEvent(EventRequest request) {
        UUID eventId = UUID.randomUUID();

        EventEnvelope envelope = new EventEnvelope(
                eventId,
                request.getType(),
                request.getRecipientId(),
                Instant.now(),
                request.getPayload(),
                1
        );

        kafkaTemplate.send("events", envelope.getRecipientId(), envelope);

        return new EventResponse(eventId);
    }
}