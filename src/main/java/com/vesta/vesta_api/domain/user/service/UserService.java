package com.vesta.vesta_api.domain.user.service;

import com.vesta.vesta_api.exception.EmailAlreadyExistsException;
import com.vesta.vesta_api.exception.InvalidCredentialsException;
import com.vesta.vesta_api.security.TokenService;
import com.vesta.vesta_api.domain.user.dto.AuthResponseDTO;
import com.vesta.vesta_api.domain.user.dto.SignInDTO;
import com.vesta.vesta_api.domain.user.dto.SignUpDTO;
import com.vesta.vesta_api.domain.user.dto.UserResponseDTO;
import com.vesta.vesta_api.domain.user.mapper.UserMapper;
import com.vesta.vesta_api.domain.user.model.User;
import com.vesta.vesta_api.domain.user.repository.UserRepository;
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
    private final TokenService tokenService;

    @Transactional
    public UserResponseDTO signUp(SignUpDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email já existente.");
        }
        User userEntity = userMapper.toEntity(request, passwordEncoder.encode(request.password()));

        userRepository.save(userEntity);

        return userMapper.toSignUpResponseDto(userEntity);
    }

    @Transactional
    public AuthResponseDTO signIn(SignInDTO request) {
        User userEntity = userRepository.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException("Email ou senha incorretos."));
        if (!passwordEncoder.matches(request.password(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha incorretos.");
        }

        return userMapper.toSignInResponseDto(userEntity, tokenService.generateToken(userEntity));
    }
}

