package com.micnusz.edns.service;


import com.micnusz.edns.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketNotificationChannel {

    private final SimpMessagingTemplate messagingTemplate;


    public void sendToUser(String recipientId, NotificationResponse response) {
        log.info("🌐 Sending WebSocket notification to user: {}", recipientId);
        messagingTemplate.convertAndSendToUser(
                recipientId,
                "/queue/notifications",
                response
        );
    }

    public void broadcast(NotificationResponse response) {
        log.info("📢 Broadcasting WebSocket notification to all users");
        messagingTemplate.convertAndSend("/topic/notifications", response);
    }
}
