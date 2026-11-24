package com.vesta.vesta_api.domain.event.controller;

import com.vesta.vesta_api.domain.event.dto.EventCreateDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.dto.EventUpdateDTO;
import com.vesta.vesta_api.domain.event.dto.EventUpdateStatusDTO;
import com.vesta.vesta_api.domain.event.model.EventStatus;
import com.vesta.vesta_api.domain.event.service.EventService;
import com.vesta.vesta_api.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RequestMapping("api/v1/events")
@RestController
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventCreateDTO request, @AuthenticationPrincipal User user) {
        EventResponseDTO response = eventService.createEvent(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        EventResponseDTO response = eventService.getById(id, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAll(@RequestParam(defaultValue = "true") boolean active,
                                                         @RequestParam(required = false) EventStatus status,
                                                         @AuthenticationPrincipal User user) {
        List<EventResponseDTO> response = eventService.getAll(active, status, user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EventUpdateDTO request, @AuthenticationPrincipal User user) {
        EventResponseDTO response = eventService.update(id, request, user);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        eventService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EventResponseDTO> changeStatus(@PathVariable Long id, @Valid @RequestBody EventUpdateStatusDTO request, @AuthenticationPrincipal User user) {
        EventResponseDTO response = eventService.changeStatus(id, request, user);
        return ResponseEntity.ok(response);
    }
}
