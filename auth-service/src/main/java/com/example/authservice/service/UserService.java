package com.example.authservice.service;

import com.example.authservice.domain.User;
import com.example.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PasswordPolicy passwordPolicy;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordPolicy passwordPolicy) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.passwordPolicy = passwordPolicy;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  @Transactional
  public User create(User user, String rawPassword) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("Email already in use");
    }
    passwordPolicy.validate(rawPassword);
    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    return userRepository.save(user);
  }

  @Transactional
  public User update(Long id, User updates) {
    User existing = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    existing.setFullName(updates.getFullName());
    existing.setRole(updates.getRole());
    if (updates.getEmail() != null && !updates.getEmail().equals(existing.getEmail())) {
      if (userRepository.existsByEmail(updates.getEmail())) {
        throw new IllegalArgumentException("Email already in use");
      }
      existing.setEmail(updates.getEmail());
    }
    return userRepository.save(existing);
  }

  @Transactional
  public void updatePassword(Long id, String rawPassword) {
    User existing = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    passwordPolicy.validate(rawPassword);
    existing.setPasswordHash(passwordEncoder.encode(rawPassword));
    userRepository.save(existing);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}


