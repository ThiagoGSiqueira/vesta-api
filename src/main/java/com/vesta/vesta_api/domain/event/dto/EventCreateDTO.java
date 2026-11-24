package com.vesta.vesta_api.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EventCreateDTO(
        @NotBlank(message = "Nome do cliente não deve ser vazio.")
        @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres.")
        String clientName,
        @Pattern(regexp = "^[0-9]+$", message = "Contato deve conter apenas números.")
        @NotBlank(message = "Contato do cliente não deve ser vazio.")
        @Size(min = 11, max = 15, message = "O contato deve conter entre 11 e 15 caracteres.")
        String contact,
        @FutureOrPresent(message = "Data deve ser no presente ou futuro.")
        @NotNull(message = "Data de agendamento não deve ser vazia.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate scheduledDate) {
}
