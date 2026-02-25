package com.micnusz.edns.event.service;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.dto.EventRequest;
import com.micnusz.edns.event.dto.EventResponse;
import com.micnusz.edns.event.producer.EventProducer;
import com.micnusz.edns.event.repository.EventRepository;
import com.micnusz.edns.metrics.EventMetrics;
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
    private final EventMetrics eventMetrics;

    public EventResponse createEvent(EventRequest request) {

        UUID eventId;

        if (request.idempotencyKey() != null && !request.idempotencyKey().isBlank()) {
            eventId = UUID.nameUUIDFromBytes(
                    request.idempotencyKey().getBytes(StandardCharsets.UTF_8)
            );

            if (eventRepository.existsById(eventId)) {
                log.info("Duplicate request ignored. idempotencyKey={}", request.idempotencyKey());

                // Metric
                eventMetrics.recordEventDuplicated();

                return new EventResponse(eventId);
            }

            log.info("Using client-provided idempotency key: {}", request.idempotencyKey());

        } else {
            eventId = UUID.randomUUID();
            log.info("No idempotency key provided, generated UUID: {}", eventId);
        }

        EventEnvelope envelope = new EventEnvelope(
                eventId,
                request.type(),
                request.recipientId(),
                Instant.now(),
                request.payload(),
                1
        );

        long startTime = System.currentTimeMillis();
        eventProducer.publish(envelope);

        // Metric
        eventMetrics.recordKafkaPublishTime(startTime);
        eventMetrics.recordEventCreated();

        log.info("Event created. eventId={} idempotencyKey={}",
                eventId,
                request.idempotencyKey() != null ? request.idempotencyKey() : "none");

        return new EventResponse(eventId);
    }
}
