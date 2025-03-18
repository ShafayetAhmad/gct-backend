package com.example.auth.service;

import com.example.auth.dto.AuthResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import com.example.auth.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        System.out.println("Registering user with email: " + request.getEmail());

        System.out.println("here");
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            System.out.println("Email already registered: " + request.getEmail());
            throw new EmailAlreadyExistsException("Email already registered");
        }

        System.out.println("here 1");
        var user = new User();

        System.out.println("here 2");
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());

        userRepository.save(user);
        System.out.println("User registered successfully: " + user.getEmail());

        var jwt = jwtUtil.generateToken(user);
        return new AuthResponse(jwt);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwt = jwtUtil.generateToken(user);
        return new AuthResponse(jwt);
    }
}