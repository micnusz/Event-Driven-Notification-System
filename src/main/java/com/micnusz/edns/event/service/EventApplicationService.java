package com.micnusz.edns.event.service;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.dto.EventRequest;
import com.micnusz.edns.event.dto.EventResponse;
import com.micnusz.edns.event.producer.EventProducer;
import com.micnusz.edns.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventApplicationService {

    private final EventProducer eventProducer;
    private final EventRepository eventRepository;

    public EventResponse createEvent(EventRequest request) {

        UUID eventId;

        if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
            eventId = UUID.nameUUIDFromBytes(
                    request.getIdempotencyKey().getBytes(StandardCharsets.UTF_8)
            );

            if (eventRepository.existsById(eventId)) {
                log.info("Duplicate request ignored. idempotencyKey={}", request.getIdempotencyKey());
                return new EventResponse(eventId);
            }

            log.info("Using client-provided idempotency key: {}", request.getIdempotencyKey());

        } else {
            eventId = UUID.randomUUID();
            log.info("No idempotency key provided, generated UUID: {}", eventId);
        }

        EventEnvelope envelope = new EventEnvelope(
                eventId,
                request.getType(),
                request.getRecipientId(),
                Instant.now(),
                request.getPayload(),
                1
        );

        eventProducer.publish(envelope);

        log.info("Event created. eventId={} idempotencyKey={}",
                eventId,
                request.getIdempotencyKey() != null ? request.getIdempotencyKey() : "none");

        return new EventResponse(eventId);
    }
}
