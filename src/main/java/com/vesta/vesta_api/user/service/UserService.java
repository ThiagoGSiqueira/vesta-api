package com.vesta.vesta_api.user.service;

import com.vesta.vesta_api.user.dto.SignUpDTO;
import com.vesta.vesta_api.user.dto.UserResponseDTO;
import com.vesta.vesta_api.user.mapper.UserMapper;
import com.vesta.vesta_api.user.model.User;
import com.vesta.vesta_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO signUp(SignUpDTO request) {
        User userEntity = userMapper.toEntity(request, passwordEncoder.encode(request.password()));

        userRepository.save(userEntity);

        return userMapper.toSignUpResponseDto(userEntity);
    }
}
