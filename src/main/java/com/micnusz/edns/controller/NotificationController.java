package com.micnusz.edns.controller;

import com.micnusz.edns.dto.NotificationResponse;
import com.micnusz.edns.service.WebSocketNotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final WebSocketNotificationChannel webSocketChannel;

    @PostMapping("/test/{userId}")
    public ResponseEntity<String> sendTestNotification(@PathVariable String userId) {
        NotificationResponse message = new NotificationResponse(
                UUID.randomUUID().toString(),
                "TEST",
                "Test Notification",
                "This is a test message",
                Instant.now().toString(),
                Map.of("test", true)
        );

        webSocketChannel.sendToUser(userId, message);
        return ResponseEntity.ok("Notification sent to user: " + userId);
    }
}