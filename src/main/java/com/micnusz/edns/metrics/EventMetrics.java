package com.micnusz.edns.metrics;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class EventMetrics {


    private final Counter eventsCreated;
    private final Counter eventsProcessed;
    private final Counter eventsFailed;
    private final Counter eventsDuplicated;
    private final Counter eventsSentToDLQ;
    private final Counter notificationsSent;
    private final Timer eventProcessingTime;
    private final Timer kafkaPublishTime;

    public EventMetrics(MeterRegistry registry) {
        // Event creation
        this.eventsCreated = Counter.builder("events.created.total")
                .description("Total number of events created via API")
                .tag("component", "api")
                .register(registry);

        // Event processing
        this.eventsProcessed = Counter.builder("events.processed.total")
                .description("Total number of events successfully processed")
                .tag("component", "consumer")
                .register(registry);

        this.eventsFailed = Counter.builder("events.failed.total")
                .description("Total number of events that failed processing")
                .tag("component", "consumer")
                .register(registry);

        this.eventsDuplicated = Counter.builder("events.duplicated.total")
                .description("Total number of duplicate events detected (idempotency)")
                .tag("component", "api")
                .register(registry);

        this.eventsSentToDLQ = Counter.builder("events.dlq.total")
                .description("Total number of events sent to Dead Letter Queue")
                .tag("component", "consumer")
                .register(registry);

        // Notifications
        this.notificationsSent = Counter.builder("notifications.sent.total")
                .description("Total number of notifications sent via WebSocket")
                .tag("component", "websocket")
                .register(registry);

        // Timers
        this.eventProcessingTime = Timer.builder("events.processing.duration")
                .description("Time taken to process an event")
                .tag("component", "consumer")
                .register(registry);

        this.kafkaPublishTime = Timer.builder("kafka.publish.duration")
                .description("Time taken to publish event to Kafka")
                .tag("component", "producer")
                .register(registry);
    }

    // Event creation metrics
    public void recordEventCreated() {
        eventsCreated.increment();
        log.debug("Metric: Event created");
    }

    public void recordEventDuplicated() {
        eventsDuplicated.increment();
        log.debug("Metric: Duplicate event detected");
    }

    // Event processing metrics
    public void recordEventProcessed() {
        eventsProcessed.increment();
        log.debug("Metric: Event processed successfully");
    }

    public void recordEventFailed() {
        eventsFailed.increment();
        log.debug("Metric: Event processing failed");
    }

    public void recordEventSentToDLQ() {
        eventsSentToDLQ.increment();
        log.warn("Metric: Event sent to DLQ");
    }

    // Notification metrics
    public void recordNotificationSent() {
        notificationsSent.increment();
        log.debug("Metric: Notification sent");
    }

    // Timing metrics
    public void recordEventProcessingTime(long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        eventProcessingTime.record(duration, TimeUnit.MILLISECONDS);
        log.debug("Metric: Event processing took {}ms", duration);
    }

    public void recordKafkaPublishTime(long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        kafkaPublishTime.record(duration, TimeUnit.MILLISECONDS);
        log.debug("Metric: Kafka publish took {}ms", duration);
    }

}
