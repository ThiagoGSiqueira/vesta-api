package com.vesta.vesta_api.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 3, max = 100, message = "A quantidade de caracter deve estar entre 3 e 100.")
    private String name;

    @Email(message = "O valor informado deve ser um e-mail.")
    @NotBlank(message = "Email não pode ser vazio.")
    @Size(max = 100, message = "A quantidade máxima de caracter é 100.")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia.")
    @Size(min = 8, max = 150, message = "A quuantidade de caracter deve estar entre 8 e 150.")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "A role não pode ser vazia.")
    private UserRole role;

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = UserRole.ROLE_USER;
    }

}
