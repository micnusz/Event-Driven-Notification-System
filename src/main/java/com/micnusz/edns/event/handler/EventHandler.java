package com.micnusz.edns.event.handler;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.service.EventPersistenceService;
import com.micnusz.edns.metrics.EventMetrics;
import com.micnusz.edns.notification.dto.NotificationCommand;
import com.micnusz.edns.notification.service.NotificationApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandler {

    private final EventPersistenceService eventPersistenceService;
    private final NotificationApplicationService notificationApplicationService;
    private final EventMetrics eventMetrics;

    @Transactional
    public void handle(EventEnvelope envelope) {

        long startTime = System.currentTimeMillis();

        try {
            eventPersistenceService.save(envelope);

        } catch (DataIntegrityViolationException exception) {
            log.info("Duplicate event detected. eventId={}", envelope.eventId());

            //Metric
            eventMetrics.recordEventDuplicated();
            return;
        }

        NotificationCommand notificationCommand = NotificationCommand.from(envelope);
        notificationApplicationService.handle(notificationCommand);

        //Metric
        eventMetrics.recordEventProcessed();
        eventMetrics.recordEventProcessingTime(startTime);
    }
}

