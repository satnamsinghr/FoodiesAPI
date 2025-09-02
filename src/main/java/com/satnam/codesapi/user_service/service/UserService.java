package com.satnam.codesapi.user_service.service;

import com.satnam.codesapi.user_service.entity.User;
import com.satnam.codesapi.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteByEmail(String email) {
        userRepository.findByEmail(email).ifPresent(userRepository::delete);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
