package tech.mayanktiwari.deployer.common.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
@Slf4j
public class HttpRequestLoggingInterceptor implements ClientHttpRequestInterceptor {

  private static final String MASKED = "[MASKED]";

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    logRequest(request, body);
    long startTime = System.currentTimeMillis();

    ClientHttpResponse response = execution.execute(request, body);

    // Buffer the response body so it can be read by both the logger and the caller
    byte[] responseBody = StreamUtils.copyToByteArray(response.getBody());
    long duration = System.currentTimeMillis() - startTime;

    logResponse(request, response, responseBody, duration);

    return new BufferedClientHttpResponse(response, responseBody);
  }

  private void logRequest(HttpRequest request, byte[] body) {
    if (!log.isDebugEnabled()) return;

    log.debug(
        "→ {} {} | headers: {} | body: {}",
        request.getMethod(),
        request.getURI(),
        maskSensitiveHeaders(request.getHeaders()),
        body.length > 0 ? new String(body, StandardCharsets.UTF_8) : "<empty>");
  }

  private void logResponse(
      HttpRequest request, ClientHttpResponse response, byte[] body, long duration)
      throws IOException {
    if (!log.isDebugEnabled()) return;

    log.debug(
        "← {} {} | {} | {}ms | body: {}",
        response.getStatusCode().value(),
        response.getStatusText(),
        request.getURI(),
        duration,
        body.length > 0 ? new String(body, StandardCharsets.UTF_8) : "<empty>");
  }

  // Returns a copy of headers with Authorization value replaced so tokens don't appear in logs
  private HttpHeaders maskSensitiveHeaders(HttpHeaders headers) {
    HttpHeaders masked = new HttpHeaders();
    headers.forEach(
        (name, values) -> {
          if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
            masked.add(name, MASKED);
          } else {
            values.forEach(v -> masked.add(name, v));
          }
        });
    return masked;
  }
}
