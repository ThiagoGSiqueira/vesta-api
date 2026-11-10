package com.vesta.vesta_api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInDTO(
        @Email(message = "O valor informado deve ser um e-mail.")
        @NotBlank(message = "Email não pode ser vazio.")
        @Size(max = 100, message = "A quantidade máxima de caracter é 100.")
        String email,
        @NotBlank(message = "Senha não pode ser vazia.")
        @Size(min = 8, max = 150, message = "A quuantidade de caracter deve estar entre 8 e 150.")
        String password) {
}
