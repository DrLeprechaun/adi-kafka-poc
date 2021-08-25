package com.adikafka.http.mock.filter;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class KeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private String requestHeader;

    public KeyAuthFilter(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(requestHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
