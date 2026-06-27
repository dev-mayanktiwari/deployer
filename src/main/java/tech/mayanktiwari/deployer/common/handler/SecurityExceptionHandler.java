package tech.mayanktiwari.deployer.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;
import tech.mayanktiwari.deployer.common.response.ApiError;

@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
      throws IOException {
    writeError(response, HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, request.getRequestURI());
  }

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
      throws IOException {
    writeError(response, HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, request.getRequestURI());
  }

  private void writeError(
      HttpServletResponse response, HttpStatus status, ErrorCode errorCode, String path)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(
        response.getWriter(), ApiError.of(errorCode, errorCode.getDefaultMessage(), path));
  }
}
