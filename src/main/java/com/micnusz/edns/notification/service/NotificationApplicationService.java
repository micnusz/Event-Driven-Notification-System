package com.micnusz.edns.notification.service;

import com.micnusz.edns.notification.NotificationCommand;
import com.micnusz.edns.notification.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationApplicationService {

    private final NotificationDispatcher notificationDispatcher;

    public void handle(NotificationCommand notificationCommand) {

        log.info("Processing notification eventId={} recipient={} type={}",
                notificationCommand.eventId(),
                notificationCommand.recipientId(),
                notificationCommand.type());

        if (notificationCommand.recipientId().startsWith("dlq-")) {
            log.error("🔥 DLQ TEST: Forcing failure for recipient: {}",
                    notificationCommand.recipientId());
            throw new RuntimeException("DLQ test - simulated persistent error");
        }

        notificationDispatcher.dispatch(notificationCommand);
    }
}
