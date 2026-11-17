package com.vesta.vesta_api.domain.event.controller;

import com.vesta.vesta_api.domain.event.dto.CreateEventDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.service.EventService;
import com.vesta.vesta_api.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RequestMapping("api/v1/event")
@RestController
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody CreateEventDTO request, @AuthenticationPrincipal User user) {
        EventResponseDTO response = eventService.createEvent(request, user);
        return ResponseEntity.status(201).body(response);
    }
}
