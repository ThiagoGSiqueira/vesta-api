package com.vesta.vesta_api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
        @NotBlank(message = "Nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres.")
        String name,
        @Email(message = "O valor informado deve ser um e-mail.")
        @NotBlank(message = "E-mail não pode ser vazio.")
        @Size(max = 100, message = "O e-mail deve conter no máximo 100 caracteres.")
        String email,
        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 8, max = 150, message = "A senha deve conter entre 8 e 150 caracteres.")
        String password) {
}
