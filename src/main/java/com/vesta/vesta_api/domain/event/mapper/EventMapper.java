package com.vesta.vesta_api.domain.event.mapper;

import com.vesta.vesta_api.domain.event.dto.EventCreateDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toEntity(EventCreateDTO createEventDTO, User user) {
        return Event.builder().clientName(createEventDTO.clientName()).
                user(user).
                contact(createEventDTO.contact()).
                scheduledDate(createEventDTO.scheduledDate()).
                build();
    }

    public EventResponseDTO toResponseDto(Event eventEntity) {
        return new EventResponseDTO(
                eventEntity.getId(),
                eventEntity.getClientName(),
                eventEntity.getTotalPrice(),
                eventEntity.getContact(),
                eventEntity.getContactLink(),
                eventEntity.getScheduledDate(),
                eventEntity.getStatus()
        );
    }
}
