package com.vesta.vesta_api.domain.user.service;

import com.vesta.vesta_api.domain.user.dto.SignUpDTO;
import com.vesta.vesta_api.domain.user.dto.UserResponseDTO;
import com.vesta.vesta_api.domain.user.mapper.UserMapper;
import com.vesta.vesta_api.domain.user.model.User;
import com.vesta.vesta_api.domain.user.repository.UserRepository;
import com.vesta.vesta_api.security.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.id());
        Assertions.assertEquals("Thiago", response.name());
        Assertions.assertEquals("thiago@gmail.com", response.email());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }


}
