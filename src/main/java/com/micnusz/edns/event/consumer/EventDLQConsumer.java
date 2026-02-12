package com.micnusz.edns.event.consumer;

import com.micnusz.edns.event.dto.EventEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventDLQConsumer {

    @KafkaListener(topics = "events.DLT", groupId = "dlq-monitor")
    public void consumeDeadLetter(
            @Payload EventEnvelope envelope,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {

        log.error("Event {} failed: {}", envelope.getEventId(), error);
    }
}