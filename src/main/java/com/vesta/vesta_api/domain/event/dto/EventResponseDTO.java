package com.vesta.vesta_api.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vesta.vesta_api.domain.event.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EventResponseDTO(
        Long id,
        String clientName,
        BigDecimal totalPrice,
        String contact,
        String contactLink,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate scheduledDate,
        EventStatus status
) {
}
