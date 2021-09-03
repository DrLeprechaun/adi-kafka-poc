package com.adikafka.http.mock.filter;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyAuthSecretFilter extends AbstractPreAuthenticatedProcessingFilter {

    public static final String X_API_KEY = "x-api-key";
    public static final String X_SIGNATURE = "x-signature";

    private String key;
    private String secret;

    public KeyAuthSecretFilter() {
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        Map<String, String> creds = new HashMap<>();
        creds.put(X_API_KEY, httpServletRequest.getHeader(X_API_KEY));
        creds.put(X_SIGNATURE, httpServletRequest.getHeader(X_SIGNATURE));
        return creds;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}

/*public class KeyAuthSecretFilter extends GenericFilterBean {

    private static final String X_API_KEY = "x-api-key";
    private static final String X_API_SECRET = "x-api-secret";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String key = request.getHeader(X_API_KEY);
        String secret = request.getHeader(X_API_SECRET);

        if (optionalCustomPrincipal.isPresent()) {
            CustomAuthentication authentication = new CustomAuthentication(optionalCustomPrincipal.get());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // In either way we continue the filter chain to also apply filters that follow after our own.
        chain.doFilter(request, response);
    }
}*/
