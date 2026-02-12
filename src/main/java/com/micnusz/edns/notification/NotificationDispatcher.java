package com.micnusz.edns.notification;

import com.micnusz.edns.error.exception.InvalidNotificationTypeException;
import com.micnusz.edns.notification.builder.NotificationMessageBuilder;
import com.micnusz.edns.notification.dto.NotificationResponse;
import com.micnusz.edns.websocket.service.WebSocketNotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationDispatcher {

    private final List<NotificationMessageBuilder> notificationMessageBuilders;
    private final WebSocketNotificationChannel webSocketNotificationChannel;

    public void dispatch(NotificationCommand notificationCommand) {

        NotificationMessageBuilder builder = notificationMessageBuilders.stream()
                .filter(b -> b.supports() == notificationCommand.type())
                .findFirst()
                .orElseThrow(() -> new InvalidNotificationTypeException(notificationCommand.type()));

        String title = builder.buildTitle(notificationCommand.payload());
        String message = builder.buildMessage(notificationCommand.payload());

        NotificationResponse response = NotificationResponse.from(notificationCommand, title, message);

        webSocketNotificationChannel.sendToUser(notificationCommand.recipientId(), response);
    }
}

