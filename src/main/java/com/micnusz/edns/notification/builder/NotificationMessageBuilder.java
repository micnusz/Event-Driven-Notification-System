package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.notification.NotificationCommand;

public interface NotificationMessageBuilder {

    EventType supports();

    String buildTitle(NotificationCommand notificationCommand);

    String buildMessage(NotificationCommand notificationCommand);
}

