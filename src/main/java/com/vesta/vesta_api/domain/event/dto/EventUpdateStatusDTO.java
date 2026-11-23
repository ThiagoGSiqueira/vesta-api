package com.vesta.vesta_api.domain.event.dto;

import com.vesta.vesta_api.domain.event.model.EventStatus;
import jakarta.validation.constraints.NotNull;

public record EventUpdateStatusDTO(
        @NotNull(message = "Status não pode ser nulo.")
        EventStatus status
) {
}
