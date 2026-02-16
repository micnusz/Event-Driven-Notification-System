package com.micnusz.edns.event.consumer;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.metrics.EventMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventDLQConsumer {

    private final EventMetrics eventMetrics;

    @KafkaListener(topics = "events-dlt", groupId = "dlq-monitor",  containerFactory = "dlqKafkaListenerContainerFactory")
    public void consumeDeadLetter(
            @Payload EventEnvelope envelope,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {

        //Metric
        eventMetrics.recordEventSentToDLQ();

        try {
            log.error("""
                EVENT IN DLQ
                Event ID: {}
                Type: {}
                Recipient: {}
                Exception: {}
                """,
                    envelope != null ? envelope.getEventId() : "DESERIALIZATION_FAILED",
                    envelope != null ? envelope.getType() : "UNKNOWN",
                    envelope != null ? envelope.getRecipientId() : "UNKNOWN",
                    error
            );
        } catch (Exception e) {
            log.error("Failed to log DLQ message: {}", e.getMessage());
        }
    }
}