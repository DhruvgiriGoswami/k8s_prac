package com.example.authservice.service;

import org.springframework.stereotype.Component;

@Component
public class PasswordPolicy {
  public void validate(String password) {
    if (password == null || password.length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters");
    }
    boolean hasDigit = password.chars().anyMatch(Character::isDigit);
    boolean hasLetter = password.chars().anyMatch(Character::isLetter);
    if (!(hasDigit && hasLetter)) {
      throw new IllegalArgumentException("Password must contain letters and digits");
    }
  }
}



