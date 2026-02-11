package com.micnusz.edns.event.consumer;

import com.micnusz.edns.event.handler.EventHandler;
import com.micnusz.edns.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final EventHandler eventHandler;

    @KafkaListener(topics = "events", groupId = "notification-service")
    public void consume(EventEnvelope envelope) {
        eventHandler.handle(envelope);
    }
}

