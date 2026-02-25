package com.micnusz.edns.event.consumer;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final EventHandler eventHandler;

    @KafkaListener(
            topics = "events",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventEnvelope envelope) {
        log.info("Processing event: {}", envelope.eventId());

        eventHandler.handle(envelope);

        log.info("Event processed: {}", envelope.eventId());
    }
}

