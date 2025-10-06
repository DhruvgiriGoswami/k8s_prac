package com.example.authservice.web;

import com.example.authservice.domain.User;
import com.example.authservice.security.JwtService;
import com.example.authservice.service.UserService;
import com.example.authservice.web.dto.AuthDtos.LoginRequest;
import com.example.authservice.web.dto.AuthDtos.RegisterRequest;
import com.example.authservice.web.dto.AuthDtos.TokenResponse;
import com.example.authservice.web.dto.AuthDtos.UpdateRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/users")
  public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest req) {
    User user = new User();
    user.setEmail(req.email);
    user.setFullName(req.fullName);
    user.setRole(req.role);
    User created = userService.create(user, req.password);
    return ResponseEntity.ok(created);
  }

  @GetMapping("/users")
  public List<User> findAll() {
    return userService.findAll();
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody UpdateRequest req) {
    User updates = new User();
    updates.setFullName(req.fullName);
    updates.setRole(req.role);
    updates.setEmail(req.email);
    return ResponseEntity.ok(userService.update(id, updates));
  }

  @PostMapping("/auth/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
    User user = userService.findByEmail(req.email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    if (!passwordEncoder.matches(req.password, user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole());
    claims.put("name", user.getFullName());
    String token = jwtService.generateToken(user.getEmail(), claims);
    return ResponseEntity.ok(new TokenResponse(token));
  }
}



