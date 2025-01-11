package dev.jagan.user_management.controllers;

import dev.jagan.user_management.dtos.LoginRequest;
import dev.jagan.user_management.dtos.LoginResponse;
import dev.jagan.user_management.dtos.UserDto;
import dev.jagan.user_management.models.User;
import dev.jagan.user_management.security.JwtTokenProvider;
import dev.jagan.user_management.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        // Set the authentication object in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate a JWT token based on the authentication
        String jwt = tokenProvider.generateToken(authentication);

        userService.updateLastLoginDate(loginRequest.getEmail());
        // Return the JWT token as part of the response
        return ResponseEntity.ok(new LoginResponse(jwt));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        // Create a new user using the provided user details
        User user = userService.createUser(userDto);
        // Return the created user as part of the response
        return ResponseEntity.ok(user);
    }
}
