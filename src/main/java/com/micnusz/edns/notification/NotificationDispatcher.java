package com.micnusz.edns.notification;

import com.micnusz.edns.error.exception.InvalidNotificationTypeException;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.notification.builder.NotificationMessageBuilder;
import com.micnusz.edns.notification.dto.NotificationResponse;
import com.micnusz.edns.websocket.service.WebSocketNotificationChannel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationDispatcher {

    private final Map<EventType, NotificationMessageBuilder<? extends EventPayload>> builderMap;
    private final WebSocketNotificationChannel webSocketNotificationChannel;

    public NotificationDispatcher(List<NotificationMessageBuilder<? extends EventPayload>> builders,
                                  WebSocketNotificationChannel webSocketNotificationChannel) {
        this.builderMap = builders.stream()
                .collect(Collectors.toUnmodifiableMap(NotificationMessageBuilder::supports, b -> b));
        this.webSocketNotificationChannel = webSocketNotificationChannel;
    }

    @SuppressWarnings("unchecked")
    public void dispatch(NotificationCommand notificationCommand) {
        var builder = (NotificationMessageBuilder<EventPayload>) builderMap.get(notificationCommand.type());
        if (builder == null) {
            throw new InvalidNotificationTypeException(notificationCommand.type());
        }

        var title = builder.buildTitle(notificationCommand.payload());
        var message = builder.buildMessage(notificationCommand.payload());

        var response = NotificationResponse.from(notificationCommand, title, message);
        webSocketNotificationChannel.broadcast(response);
//      webSocketNotificationChannel.sendToUser(notificationCommand.recipientId(), response);
    }
}

