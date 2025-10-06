package com.example.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.authservice.domain.User;
import com.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@Import(UserServiceTest.Config.class)
class UserServiceTest {

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  static class Config {
    @org.springframework.context.annotation.Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
  }

  @Test
  void create_and_find_user() {
    UserService userService = new UserService(userRepository, passwordEncoder);
    User user = new User();
    user.setEmail("a@b.com");
    user.setFullName("Alpha Beta");
    user.setRole("USER");
    User created = userService.create(user, "password123");

    assertThat(created.getId()).isNotNull();
    assertThat(created.getPasswordHash()).isNotBlank();
    assertThat(passwordEncoder.matches("password123", created.getPasswordHash())).isTrue();
  }

  @Test
  void duplicate_email_throws() {
    UserService userService = new UserService(userRepository, passwordEncoder);
    User u1 = new User();
    u1.setEmail("dup@b.com");
    u1.setFullName("Dup One");
    u1.setRole("USER");
    userService.create(u1, "password123");

    User u2 = new User();
    u2.setEmail("dup@b.com");
    u2.setFullName("Dup Two");
    u2.setRole("ADMIN");

    assertThrows(IllegalArgumentException.class, () -> userService.create(u2, "password456"));
  }
}



