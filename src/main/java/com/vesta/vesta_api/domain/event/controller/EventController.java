package com.vesta.vesta_api.domain.event.controller;

import com.vesta.vesta_api.domain.event.dto.CreateEventDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.dto.EventUpdateDTO;
import com.vesta.vesta_api.domain.event.service.EventService;
import com.vesta.vesta_api.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        EventResponseDTO response = eventService.getById(id, user);
        return ResponseEntity.ok(response);
     }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAll(@AuthenticationPrincipal User user) {
        List<EventResponseDTO> response = eventService.getAll(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id, @AuthenticationPrincipal User user, @Valid @RequestBody EventUpdateDTO request) {
        EventResponseDTO response = eventService.update(id, user, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        eventService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
