package com.vesta.vesta_api.domain.user.dto;

import com.vesta.vesta_api.domain.user.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres.")
        String name,

        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "Informe um endereço de e-mail válido.")
        @Size(max = 100, message = "O e-mail deve conter no máximo 100 caracteres.")
        String email,

        @ValidPassword
        String password) {
}
