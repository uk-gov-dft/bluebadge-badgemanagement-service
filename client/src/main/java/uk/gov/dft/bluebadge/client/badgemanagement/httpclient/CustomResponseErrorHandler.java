package uk.gov.dft.bluebadge.client.badgemanagement.httpclient;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    switch (response.getStatusCode()) {
      case OK:
        // Validation errors treated as ok.  Error mapped into CommonResponse.error
      case BAD_REQUEST:
        {
          return false;
        }
    }
    return true;
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    super.handleError(response);
  }
}
