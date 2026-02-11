package com.micnusz.edns.event.controller;


import com.micnusz.edns.event.dto.EventRequest;
import com.micnusz.edns.event.dto.EventResponse;
import com.micnusz.edns.event.service.EventApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventApplicationService eventApplicationService;


    @PostMapping()
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request) {
        EventResponse response = eventApplicationService.createEvent(request);
        return ResponseEntity.created(URI.create("/api/events/" + response.getEventId())).body(response);
    }
}
