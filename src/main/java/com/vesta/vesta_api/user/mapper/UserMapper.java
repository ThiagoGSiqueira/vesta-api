package com.vesta.vesta_api.user.mapper;

import com.vesta.vesta_api.user.dto.AuthResponseDTO;
import com.vesta.vesta_api.user.dto.SignUpDTO;
import com.vesta.vesta_api.user.dto.UserResponseDTO;
import com.vesta.vesta_api.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(SignUpDTO signUpDTO, String encryptedPassword) {
        return User.builder()
                .name(signUpDTO.name())
                .email(signUpDTO.email())
                .password(encryptedPassword)
                .build();
    }

    public UserResponseDTO toSignUpResponseDto(User userEntity) {
        return new UserResponseDTO(userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail());
    }

    public AuthResponseDTO toSignInResponseDto(User userEntity, String token) {
        return new AuthResponseDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                token
        );
    }
}
