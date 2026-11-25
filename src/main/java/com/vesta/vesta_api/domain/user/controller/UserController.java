package com.vesta.vesta_api.domain.user.controller;

import com.vesta.vesta_api.domain.user.dto.*;
import com.vesta.vesta_api.domain.user.model.User;
import com.vesta.vesta_api.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RequestMapping("/api/v1/users")
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

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User user) {
        UserResponseDTO response = userService.getMe(user.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UserUpdateDTO request, @AuthenticationPrincipal User user) {
        UserResponseDTO response = userService.update(request, user.getId());
        return ResponseEntity.ok(response);
    }
}
