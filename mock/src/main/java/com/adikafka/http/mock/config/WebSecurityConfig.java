package com.adikafka.http.mock.config;

import com.adikafka.http.mock.authmanagers.KeyAuthManager;
import com.adikafka.http.mock.authmanagers.KeyAuthSecretManager;
import com.adikafka.http.mock.filter.KeyAuthFilter;
import com.adikafka.http.mock.filter.KeyAuthSecretFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Configuration
    @Order(1)
    public static class KeyAuthSecretConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            KeyAuthSecretFilter filter = new KeyAuthSecretFilter();
            filter.setAuthenticationManager(new KeyAuthSecretManager());

            httpSecurity.
                    antMatcher("/auth/keysecret").
                    csrf().disable().
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                    and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
        }
    }

    @Configuration
    @Order(2)
    public static class KeyAuthConfiguration extends WebSecurityConfigurerAdapter {

        private String requestHeader = "api_key";

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            KeyAuthFilter filter = new KeyAuthFilter(requestHeader);
            filter.setAuthenticationManager(new KeyAuthManager());

            httpSecurity.
                    antMatcher("/auth/key").
                    csrf().disable().
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                    and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
        }
    }

    @Configuration
    @Order(3)
    public static class BasicAuthConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/auth/basic").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http
                    .logout()
                    .logoutUrl("/logout/do")
                    .logoutSuccessUrl("/logout/ok")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID");
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth)
                throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("user")
                    .password("{noop}password")
                    .roles("USER");
        }
    }
}
