package com.vesta.vesta_api.domain.event.service;

import com.vesta.vesta_api.domain.event.dto.CreateEventDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.dto.EventUpdateDTO;
import com.vesta.vesta_api.domain.event.mapper.EventMapper;
import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.event.repository.EventRepository;
import com.vesta.vesta_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponseDTO createEvent(CreateEventDTO request, User user) {
        Event eventEntity = eventMapper.toEntity(request, user);

        eventRepository.save(eventEntity);

        return eventMapper.toResponseDto(eventEntity);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getById(Long id, User user) {
        Event eventEntity = eventRepository.findByIdAndUser(id, user);
        return eventMapper.toResponseDto(eventEntity);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAll(User user) {
        List<Event> events = eventRepository.findAllByUser(user);

        return events.stream().map(eventMapper::toResponseDto).toList();
    }

    @Transactional
    public EventResponseDTO update(Long id, User user, EventUpdateDTO request) {
        Event eventEntity = eventRepository.findByIdAndUser(id, user);

        if(request.clientName() != null && !request.clientName().isBlank()) {
            eventEntity.renameClient(request.clientName());
        }

        if(request.contact() != null && !request.contact().isBlank()) {
            eventEntity.changeContact(request.contact());
        }

        if(request.scheduledDate() != null) {
            eventEntity.reschedule(request.scheduledDate());
        }

        eventRepository.save(eventEntity);

        return eventMapper.toResponseDto(eventEntity);
    }
}
