package com.vesta.vesta_api.domain.user.service;

import com.vesta.vesta_api.domain.user.dto.AuthResponseDTO;
import com.vesta.vesta_api.domain.user.dto.SignInDTO;
import com.vesta.vesta_api.domain.user.dto.SignUpDTO;
import com.vesta.vesta_api.domain.user.dto.UserResponseDTO;
import com.vesta.vesta_api.domain.user.mapper.UserMapper;
import com.vesta.vesta_api.domain.user.model.User;
import com.vesta.vesta_api.domain.user.repository.UserRepository;
import com.vesta.vesta_api.exception.EmailAlreadyExistsException;
import com.vesta.vesta_api.exception.InvalidCredentialsException;
import com.vesta.vesta_api.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String USERNAME = "Test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnSignUpResponseDTO() {
        SignUpDTO request = new SignUpDTO(USERNAME, EMAIL, PASSWORD);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 1L);
            return user;
        });

        UserResponseDTO response = userService.signUp(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(USERNAME, response.name());
        assertEquals(EMAIL, response.email());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void shouldThrowEmailAlreadyExistsException() {
        SignUpDTO request = new SignUpDTO(USERNAME, EMAIL, PASSWORD);

        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        EmailAlreadyExistsException e = assertThrows(EmailAlreadyExistsException.class, () -> userService.signUp(request));
        assertEquals("Email já existente.", e.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnAuthResponseDTO() {
        SignInDTO request = new SignInDTO(EMAIL, PASSWORD);
        String token = "false-jwt-token";

        when(userRepository.findByEmail(EMAIL)).thenAnswer(invocation -> {
            User user = User.builder().name(USERNAME).
                    email(EMAIL).
                    password(ENCODED_PASSWORD).
                    build();
            ReflectionTestUtils.setField(user, "id", 1L);

            return Optional.of(user);
        });

        when(tokenService.generateToken(any(User.class))).thenReturn(token);
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);


        AuthResponseDTO response = userService.signIn(request);

        verify(userRepository).findByEmail(EMAIL);
        assertEquals(1L, response.id());
        assertEquals(USERNAME, response.name());
        assertEquals(EMAIL, response.email());
        assertEquals(token, response.token());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        SignInDTO request = new SignInDTO(EMAIL, PASSWORD);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> userService.signIn(request));

        assertEquals("Email ou senha incorretos.", e.getMessage());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenPasswordNotMatches() {
        SignInDTO request = new SignInDTO(EMAIL, PASSWORD);


        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(
                User.builder().name(USERNAME).email(EMAIL).password(ENCODED_PASSWORD).build()
        ));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.signIn(request));

    }
}
