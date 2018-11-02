package uk.gov.dft.bluebadge.service.badgemanagement.esapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class EsapiFilter extends OncePerRequestFilter {

  /**
   * Wraps the request/response using a secure implementation that sanitises the
   * headers/cookies/parameters etc..
   *
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
    } else {
      doFilterWrapped(wrapRequest(request), response, filterChain);
    }
  }

  protected void doFilterWrapped(
    XssRequestWrapper request, HttpServletResponse response, FilterChain filterChain) {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error("Unable to complete filter chain:", e);
    }
  }

  private XssRequestWrapper wrapRequest(HttpServletRequest request) {
    if (request instanceof XssRequestWrapper) {
      return (XssRequestWrapper) request;
    } else {
      return new XssRequestWrapper(request);
    }
  }
}
