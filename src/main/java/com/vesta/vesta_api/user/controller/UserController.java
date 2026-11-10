package com.vesta.vesta_api.user.controller;

import com.vesta.vesta_api.user.dto.AuthResponseDTO;
import com.vesta.vesta_api.user.dto.SignInDTO;
import com.vesta.vesta_api.user.dto.SignUpDTO;
import com.vesta.vesta_api.user.dto.UserResponseDTO;
import com.vesta.vesta_api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RequestMapping("/api/v1/auth")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDTO> signUp(@Valid @RequestBody SignUpDTO request) {
        UserResponseDTO response = userService.signUp(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> signIn(@Valid @RequestBody SignInDTO request) {
        AuthResponseDTO response = userService.signIn(request);
        return ResponseEntity.ok(response);
    }
}
