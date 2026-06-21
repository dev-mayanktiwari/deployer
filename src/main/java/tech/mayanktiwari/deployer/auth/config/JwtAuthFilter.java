package tech.mayanktiwari.deployer.auth.config;

import static tech.mayanktiwari.deployer.common.config.Constants.AUTHORIZATION;
import static tech.mayanktiwari.deployer.common.config.Constants.AUTH_COOKIE;
import static tech.mayanktiwari.deployer.common.config.Constants.BEARER;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.mayanktiwari.deployer.auth.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
      filterChain.doFilter(request, response);
      return;
    }

    extractToken(request)
        .flatMap(jwtService::extractPrincipal)
        .ifPresent(
            principal -> {
              UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
              authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authentication);
            });

    filterChain.doFilter(request, response);
  }

  private Optional<String> extractToken(HttpServletRequest request) {
    // 1. Authorization: Bearer <token> header (API / mobile clients)
    String authHeader = request.getHeader(AUTHORIZATION);
    if (Objects.nonNull(authHeader) && authHeader.startsWith(BEARER)) {
      return Optional.of(authHeader.substring(BEARER.length()));
    }

    // 2. HttpOnly cookie (browser clients)
    if (Objects.nonNull(request.getCookies())) {
      return Arrays.stream(request.getCookies())
          .filter(c -> AUTH_COOKIE.equals(c.getName()))
          .map(Cookie::getValue)
          .findFirst();
    }

    return Optional.empty();
  }
}
