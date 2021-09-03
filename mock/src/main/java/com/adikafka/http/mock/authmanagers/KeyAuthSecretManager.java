package com.adikafka.http.mock.authmanagers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

import static com.adikafka.http.mock.filter.KeyAuthSecretFilter.X_API_KEY;
import static com.adikafka.http.mock.filter.KeyAuthSecretFilter.X_SIGNATURE;

public class KeyAuthSecretManager implements AuthenticationManager {

    private static final String KEY_VALUE = "0&RvPza%2k8";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Map<String, String> principal = (Map<String, String>) authentication.getPrincipal();
        if (!KEY_VALUE.equals(principal.get(X_API_KEY)) || principal.get(X_SIGNATURE) == null) {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        }
        authentication.setAuthenticated(true);
        return authentication;
    }
}
