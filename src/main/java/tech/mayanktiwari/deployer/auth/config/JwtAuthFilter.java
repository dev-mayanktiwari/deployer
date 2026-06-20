package tech.mayanktiwari.deployer.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;
import tech.mayanktiwari.deployer.auth.service.JwtService;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import static tech.mayanktiwari.deployer.common.config.Constants.AUTHORIZATION;
import static tech.mayanktiwari.deployer.common.config.Constants.BEARER;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(BEARER.length());

        if (!jwtService.validateToken(jwtToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthPrincipal principal = new AuthPrincipal(
                UUID.fromString(jwtService.extractUserId(jwtToken)),
                jwtService.extractUsername(jwtToken)
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                Collections.emptyList()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
