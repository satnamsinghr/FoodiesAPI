package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.dto.AuthRequest;
import com.satnam.codesapi.user_service.dto.AuthResponse;
import com.satnam.codesapi.user_service.dto.RegisterRequest;
import com.satnam.codesapi.user_service.dto.UpdateUserRequest;
import com.satnam.codesapi.user_service.entity.User;
import com.satnam.codesapi.user_service.service.UserService;
import com.satnam.codesapi.user_service.config.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.getUserByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role("ROLE_USER")   // store role with ROLE_ prefix for Spring convention
                .build();

        User saved = userService.saveUser(user);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    // Login => returns JWT
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // Protected - returns current user info (email principal)
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).body("Not authenticated");
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // ------------------ new endpoints ------------------

    // Update current user (owner)
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Authentication authentication,
                                      @RequestBody UpdateUserRequest req) {
        if (authentication == null) return ResponseEntity.status(401).body("Not authenticated");
        String email = authentication.getName();
        User u = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getName() != null && !req.getName().isBlank()) u.setName(req.getName());
        if (req.getPhone() != null && !req.getPhone().isBlank()) u.setPhone(req.getPhone());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        User saved = userService.saveUser(u);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    // Delete current user (owner)
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).body("Not authenticated");
        String email = authentication.getName();
        userService.deleteByEmail(email);
        return ResponseEntity.ok("User account deleted");
    }
}
