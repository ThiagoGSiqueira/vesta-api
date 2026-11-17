package com.vesta.vesta_api.domain.event.service;

import com.vesta.vesta_api.domain.event.dto.CreateEventDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.mapper.EventMapper;
import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.event.repository.EventRepository;
import com.vesta.vesta_api.domain.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Transactional
    public List<EventResponseDTO> getAllByUser(User user) {
        List<Event> events = eventRepository.findAllByUser(user);

        return events.stream().map(eventMapper::toResponseDto).toList();
    }
}
