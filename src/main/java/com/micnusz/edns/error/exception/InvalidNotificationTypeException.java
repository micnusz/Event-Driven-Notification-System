package com.micnusz.edns.error.exception;

import com.micnusz.edns.event.enums.EventType;

public class InvalidNotificationTypeException extends RuntimeException {
    public InvalidNotificationTypeException(EventType type) {
        super("No builder for type: " + type);
    }
}

