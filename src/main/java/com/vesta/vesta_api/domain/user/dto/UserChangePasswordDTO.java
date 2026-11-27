package com.vesta.vesta_api.domain.user.dto;

import com.vesta.vesta_api.domain.user.validation.ValidPassword;


public record UserChangePasswordDTO(
        @ValidPassword
        String currentPassword,

        @ValidPassword
        String newPassword,

        @ValidPassword
        String confirmPassword
) {
}
