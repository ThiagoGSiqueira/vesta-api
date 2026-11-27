package com.vesta.vesta_api.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres.")
        String name,
        @Email(message = "Informe um endereço de e-mail válido.")
        @Size(max = 100, message = "O e-mail deve conter no máximo 100 caracteres.")
        String email
) {
}
