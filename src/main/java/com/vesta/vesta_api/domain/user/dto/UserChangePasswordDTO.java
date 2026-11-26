package com.vesta.vesta_api.domain.user.dto;

public record UserChangePasswordDTO(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
