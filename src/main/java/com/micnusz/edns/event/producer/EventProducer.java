package com.micnusz.edns.event.producer;

import com.micnusz.edns.event.dto.EventEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProducer {

    private final KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    public void publish(EventEnvelope envelope) {
        kafkaTemplate.send("events", envelope.recipientId(), envelope)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event {}", envelope.eventId(), ex);
                    }
                });
    }
}
