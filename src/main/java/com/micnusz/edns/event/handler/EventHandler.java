package com.micnusz.edns.event.handler;

import com.micnusz.edns.event.service.EventPersistenceService;
import com.micnusz.edns.model.EventEnvelope;
import com.micnusz.edns.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler {

    private final EventPersistenceService eventPersistenceService;
    private final NotificationService notificationService;

    public void handle(EventEnvelope envelope) {

        eventPersistenceService.save(envelope);

        notificationService.processEvent(envelope);
    }
}

