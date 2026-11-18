package com.vesta.vesta_api.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateEventDTO(
        @NotBlank(message = "Nome do cliente não deve ser vazio.")
        @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres.")
        String clientName,
        @NotBlank(message = "Contato do cliente não deve ser vazio.")
        @Size(min = 11, max = 20, message = "O contato deve conter entre 11 e 20 caracteres.")
        String contact,
        @NotNull(message = "Data de agendamento não deve ser vazia.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate scheduledDate) {
}
