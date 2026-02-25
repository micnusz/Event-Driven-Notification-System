package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.enums.EventType;

public interface NotificationMessageBuilder<T extends  EventPayload> {

    EventType supports();
    String buildTitle(T payload);
    String buildMessage(T payload);
}

