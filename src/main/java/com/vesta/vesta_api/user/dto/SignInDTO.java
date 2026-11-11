package com.vesta.vesta_api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInDTO(
        @Email(message = "O valor informado deve ser um e-mail.")
        @NotBlank(message = "E-mail não pode ser vazio.")
        String email,
        @NotBlank(message = "A senha não pode ser vazia.")
        String password) {
}
