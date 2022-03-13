package net.ancronik.cookbook.backend.web.filter;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.Constants;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter for checking and setting logging HTTP headers.
 *
 * @author Nikola Presecki
 */
@Component
@Order(1)
@Slf4j
public class RequestResponseClidFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String clid = request.getHeader(Constants.HEADER_CLID);
        if (!StringUtils.hasText(clid)) {
            LOG.warn("No CLID in request, generating new one");
            clid = UUID.randomUUID().toString();
        }

        MDC.put(Constants.LOGGING_CLID, clid);

        filterChain.doFilter(servletRequest, servletResponse);

        response.setHeader(Constants.HEADER_CLID, clid);
    }
}
