package tech.mayanktiwari.deployer.auth.service;

import static tech.mayanktiwari.deployer.common.config.Constants.USERNAME;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;

@Service
@Slf4j
public class JwtService {

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-ms}")
  private long expiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public String generateToken(UUID userId, String username) {
    return Jwts.builder()
        .subject(userId.toString())
        .claim(USERNAME, username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey())
        .compact();
  }

  public Optional<AuthPrincipal> extractPrincipal(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
      return Optional.of(
          new AuthPrincipal(
              UUID.fromString(claims.getSubject()), claims.get(USERNAME, String.class)));
    } catch (Exception e) {
      log.error("Token validation failed ::: {}", e.getMessage());
      return Optional.empty();
    }
  }
}
