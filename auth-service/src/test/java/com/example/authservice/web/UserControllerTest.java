package com.example.authservice.web;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.authservice.domain.User;
import com.example.authservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired private org.springframework.test.web.servlet.MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @Test
  void register_and_login_returns_token() throws Exception {
    var reg = new java.util.HashMap<String, Object>();
    reg.put("email", "test@example.com");
    reg.put("password", "password123");
    reg.put("fullName", "Test User");
    reg.put("role", "USER");

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(reg)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", notNullValue()));

    var login = new java.util.HashMap<String, Object>();
    login.put("email", "test@example.com");
    login.put("password", "password123");

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(login)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token", notNullValue()));
  }
}



