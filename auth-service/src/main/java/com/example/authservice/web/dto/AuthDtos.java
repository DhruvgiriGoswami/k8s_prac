package com.example.authservice.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
  public static class RegisterRequest {
    @NotBlank @Email public String email;
    @NotBlank @Size(min = 8) public String password;
    @NotBlank public String fullName;
    @NotBlank public String role;
  }

  public static class UpdateRequest {
    @NotBlank public String fullName;
    @NotBlank public String role;
    public String email; // optional change
  }

  public static class LoginRequest {
    @NotBlank @Email public String email;
    @NotBlank public String password;
  }

  public static class TokenResponse {
    public String token;
    public TokenResponse(String token) { this.token = token; }
  }
}



