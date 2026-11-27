package com.vesta.vesta_api.domain.user.service;

import com.vesta.vesta_api.domain.user.dto.*;
import com.vesta.vesta_api.exception.BusinessRuleException;
import com.vesta.vesta_api.exception.EmailAlreadyExistsException;
import com.vesta.vesta_api.exception.InvalidCredentialsException;
import com.vesta.vesta_api.exception.ResourceNotFoundException;
import com.vesta.vesta_api.security.TokenService;
import com.vesta.vesta_api.domain.user.mapper.UserMapper;
import com.vesta.vesta_api.domain.user.model.User;
import com.vesta.vesta_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
        User user = userMapper.toEntity(request, passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Transactional
    public AuthResponseDTO signIn(SignInDTO request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException("Email ou senha incorretos."));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha incorretos.");
        }

        return userMapper.toSignInResponseDto(user, tokenService.generateToken(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getMe(Long id) {
        return userMapper.toResponseDto(findById(id));
    }

    @Transactional
    public UserResponseDTO update(UserUpdateDTO request, Long id) {
        User user = findById(id);

        user.renameClient(request.name());
        user.changeEmail(request.email());
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public void changePassword(UserChangePasswordDTO request, Long id) {
        User user = findById(id);

        if (!Objects.equals(request.newPassword(), request.confirmPassword())) {
            throw new BusinessRuleException("A nova senha e a confirmação não coincidem.");
        }
        if (Objects.equals(request.newPassword(), request.currentPassword())) {
            throw new BusinessRuleException("A nova senha não pode ser igual à senha atual.");
        }
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessRuleException("A senha atual está incorreta.");
        } else {
            user.changePassword(passwordEncoder.encode(request.newPassword()));
        }
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);

        user.delete();
    }

    private User findById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }
}

