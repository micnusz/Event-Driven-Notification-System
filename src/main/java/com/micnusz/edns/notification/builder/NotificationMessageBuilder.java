package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.enums.EventType;

public interface NotificationMessageBuilder {

    EventType supports();

    String buildTitle(EventPayload payload);

    String buildMessage(EventPayload payload);
}

