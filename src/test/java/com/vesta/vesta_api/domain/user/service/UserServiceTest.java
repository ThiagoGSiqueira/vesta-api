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

    private static final String EMAIL = "test@test.com";

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
        SignUpDTO request = new SignUpDTO("Thiago", "thiago@gmail.com", "thiago123");

        when(passwordEncoder.encode("thiago123")).thenReturn("EncryptedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 1L);
            return user;
        });

        UserResponseDTO response = userService.signUp(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Thiago", response.name());
        assertEquals("thiago@gmail.com", response.email());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void shouldThrowEmailAlreadyExistsException() {
        SignUpDTO request = new SignUpDTO("isabele", "isabele@gmail.com", "isabele123");

        when(userRepository.existsByEmail("isabele@gmail.com")).thenReturn(true);

        EmailAlreadyExistsException e = assertThrows(EmailAlreadyExistsException.class, () -> userService.signUp(request));
        assertEquals("Email já existente.", e.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnAuthResponseDTO() {
        SignInDTO request = new SignInDTO("thiago@gmail.com", "thiago123");

        when(userRepository.findByEmail("thiago@gmail.com")).thenAnswer(invocation -> {
            User user = User.builder().name("Thiago").
                    email("thiago@gmail.com").
                    password("encryptedPassword").
                    build();
            ReflectionTestUtils.setField(user, "id", 1L);

            return Optional.of(user);
        });

        when(tokenService.generateToken(any(User.class))).thenReturn("false-jwt-token");
        when(passwordEncoder.matches("thiago123", "encryptedPassword")).thenReturn(true);


        AuthResponseDTO response = userService.signIn(request);

        verify(userRepository).findByEmail("thiago@gmail.com");
        assertEquals(1L, response.id());
        assertEquals("Thiago", response.name());
        assertEquals("thiago@gmail.com", response.email());
        assertEquals("false-jwt-token", response.token());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        SignInDTO request = new SignInDTO("thiago@gmail.com", "thiago123");

        when(userRepository.findByEmail("thiago@gmail.com")).thenReturn(Optional.empty());

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> userService.signIn(request));

        assertEquals("Email ou senha incorretos.", e.getMessage());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenPasswordNotMatches() {
        String rawPassword = "test123";
        String encodedPassword = "encodedPassword";
        SignInDTO request = new SignInDTO(EMAIL, rawPassword);


        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(
                User.builder().name("Test").email(EMAIL).password(encodedPassword).build()
        ));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.signIn(request));

    }
}
