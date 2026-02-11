package com.micnusz.edns.event.producer;

import com.micnusz.edns.event.dto.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    public void publish(EventEnvelope envelope) {
        kafkaTemplate.send("events", envelope.getRecipientId(), envelope);
    }
}
