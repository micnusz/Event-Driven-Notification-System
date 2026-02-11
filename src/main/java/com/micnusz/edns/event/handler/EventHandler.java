package com.micnusz.edns.event.handler;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.service.EventPersistenceService;
import com.micnusz.edns.notification.NotificationCommand;
import com.micnusz.edns.notification.service.NotificationApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler {

    private final EventPersistenceService eventPersistenceService;
    private final NotificationApplicationService notificationApplicationService;

    public void handle(EventEnvelope envelope) {

        eventPersistenceService.save(envelope);

        NotificationCommand notificationCommand = NotificationCommand.from(envelope);
        notificationApplicationService.handle(notificationCommand);
    }
}

