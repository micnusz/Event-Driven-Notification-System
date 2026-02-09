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
        String now = Instant.now().toString();

        EventEnvelope envelope = new EventEnvelope(
                eventId,
                request.type(),
                request.recipientId(),
                now,
                request.payload(),
                1
        );

        kafkaTemplate.send("events", envelope.recipientId(), envelope);

        return new EventResponse(eventId);
    }
}