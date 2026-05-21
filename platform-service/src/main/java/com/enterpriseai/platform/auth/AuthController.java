package com.enterpriseai.platform.auth;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterpriseai.platform.security.CurrentUser;
import com.enterpriseai.platform.security.JwtService;
import com.enterpriseai.platform.user.UserAccount;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUser currentUser;

    AuthController(AuthenticationManager authenticationManager, JwtService jwtService, CurrentUser currentUser) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.currentUser = currentUser;
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        UserAccount user = (UserAccount) authentication.getPrincipal();
        return ResponseEntity.ok(AuthResponse.from(user, jwtService.generate(user)));
    }

    @GetMapping("/me")
    ResponseEntity<AuthResponse.UserView> me() {
        return ResponseEntity.ok(AuthResponse.UserView.from(currentUser.get()));
    }
}
