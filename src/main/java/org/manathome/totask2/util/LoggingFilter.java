package org.manathome.totask2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ch.qos.logback.classic.ClassicConstants;

/**
 * a servlet filter that inserts various values into the logback logging MDC.
 * - %M{user} and 
 * - %M{REQUEST_REMOTE_HOST_MDC_KEY}.
 * 
 * @author man-at-home
 * @since  2015-03-05
 */
public class LoggingFilter implements Filter {
    
  private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

  public void destroy() {
      LOG.debug("destroy filter");
  }

  /** logback data enrichment. */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
          throws IOException, ServletException {

    insertIntoMDC(request);

    try {
      chain.doFilter(request, response);
    } finally {
      clearMDC();
    }
  }

  /** insert additional logging information. */
  void insertIntoMDC(ServletRequest request) {

    MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteHost());

    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      HttpSession session = httpServletRequest.getSession(false);
      
      if (session != null) {
         
          SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
              
          if (sci != null) {
              MDC.put("log.user", sci.getAuthentication().getName());
          } else {
              MDC.put("log.user", session == null ? "*nosci" : session.getId());
          }
      } else {
          MDC.put("log.user", session == null ? "*nosession" : session.getId());           
      }
      
    } else {
        MDC.put("log.user", "*norequest"); 
    }
    
  }

  /** remove additional logging data after request. */
  void clearMDC() {
    MDC.remove(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY);
    MDC.remove("log.user");
  }

  public void init(FilterConfig fc) throws ServletException {
      LOG.debug("init filter" + (fc == null ? "?" : fc.getFilterName()));
  }
}
