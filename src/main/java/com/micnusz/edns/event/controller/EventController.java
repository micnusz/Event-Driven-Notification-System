package com.micnusz.edns.event.controller;


import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.dto.EventHistoryResponse;
import com.micnusz.edns.event.dto.EventRequest;
import com.micnusz.edns.event.dto.EventResponse;
import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.event.repository.EventRepository;
import com.micnusz.edns.event.service.EventApplicationService;
import com.micnusz.edns.notification.builder.NotificationMessageBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventApplicationService eventApplicationService;
    private final EventRepository eventRepository;
    private final List<NotificationMessageBuilder> notificationBuilders;


    @PostMapping()
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventApplicationService.createEvent(request);
        return ResponseEntity.created(URI.create("/api/events/" + response.eventId())).body(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<EventHistoryResponse>> getRecentEvents(
            @RequestParam(defaultValue = "50") int limit) {

        Pageable pageable = PageRequest.of(0, limit);
        List<EventEntity> recentEvents = eventRepository.findAllByOrderByOccurredAtDesc(pageable);

        List<EventHistoryResponse> response = recentEvents.stream()
                .map(entity -> EventHistoryResponse.from(entity, notificationBuilders))
                .toList();

        return ResponseEntity.ok(response);
    }
}
