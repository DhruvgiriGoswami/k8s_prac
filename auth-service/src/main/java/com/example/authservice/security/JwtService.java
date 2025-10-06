package com.example.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
  private final Key key;
  private final long expirationSeconds;

  public JwtService(
      @Value("${security.jwt.secret}") String base64Secret,
      @Value("${security.jwt.expirationSeconds:3600}") long expirationSeconds) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(String subject, Map<String, Object> claims) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(subject)
        .addClaims(claims)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}


