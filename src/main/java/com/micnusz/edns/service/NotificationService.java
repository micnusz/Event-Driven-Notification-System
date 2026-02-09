package com.micnusz.edns.service;

import com.micnusz.edns.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public void processEvent(EventEnvelope envelope) {
    }
}
