package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.dto.UpdateUserRequest;
import com.satnam.codesapi.user_service.entity.User;
import com.satnam.codesapi.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    // Helper to test admin authority
    private boolean isAdmin(Authentication auth) {
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // GET /api/users  -> ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    // GET /api/users/{id} -> admin OR the user themself
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication auth) {
        User u = userService.getUserById(id).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();

        String principalEmail = auth != null ? auth.getName() : null;
        if (!u.getEmail().equals(principalEmail) && !isAdmin(auth)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        u.setPassword(null);
        return ResponseEntity.ok(u);
    }

    // PUT /api/users/{id} -> update profile (owner or admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UpdateUserRequest req,
                                        Authentication auth) {
        User u = userService.getUserById(id).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();

        String principalEmail = auth != null ? auth.getName() : null;
        if (!u.getEmail().equals(principalEmail) && !isAdmin(auth)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        if (req.getName() != null) u.setName(req.getName());
        if (req.getPhone() != null) u.setPhone(req.getPhone());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        User saved = userService.saveUser(u);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/users/{id} -> ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) return ResponseEntity.notFound().build();
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
