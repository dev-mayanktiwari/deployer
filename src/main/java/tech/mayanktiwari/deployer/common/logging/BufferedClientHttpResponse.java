package tech.mayanktiwari.deployer.common.logging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Wraps a ClientHttpResponse with a pre-read body so the stream can be consumed again by the actual
 * caller after the logging interceptor has already read it.
 */
class BufferedClientHttpResponse implements ClientHttpResponse {

  private final ClientHttpResponse delegate;
  private final byte[] body;

  BufferedClientHttpResponse(ClientHttpResponse delegate, byte[] body) {
    this.delegate = delegate;
    this.body = body;
  }

  @Override
  public HttpStatusCode getStatusCode() throws IOException {
    return delegate.getStatusCode();
  }

  @Override
  public String getStatusText() throws IOException {
    return delegate.getStatusText();
  }

  @Override
  public HttpHeaders getHeaders() {
    return delegate.getHeaders();
  }

  @Override
  public InputStream getBody() {
    return new ByteArrayInputStream(body);
  }

  @Override
  public void close() {
    delegate.close();
  }
}
