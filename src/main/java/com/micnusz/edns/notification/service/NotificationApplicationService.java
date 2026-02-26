package com.micnusz.edns.notification.service;

import com.micnusz.edns.notification.dto.NotificationCommand;
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

        notificationDispatcher.dispatch(notificationCommand);
    }
}
