package com.micnusz.edns.event.service;

import com.micnusz.edns.event.dto.EventRequest;
import com.micnusz.edns.event.dto.EventResponse;
import com.micnusz.edns.event.producer.EventProducer;
import com.micnusz.edns.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.time.Instant.*;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final EventProducer eventProducer;

    public EventResponse createEvent(EventRequest request) {

        UUID eventId = UUID.randomUUID();

        EventEnvelope envelope = new EventEnvelope(
                eventId,
                request.getType(),
                request.getRecipientId(),
                now(),
                request.getPayload(),
                1
        );

        eventProducer.publish(envelope);

        return new EventResponse(eventId);
    }
}
