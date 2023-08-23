package com.example.bookwonders.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    public WithSecurityContextFactory<WithMockUser> withMockUserSecurityContextFactory() {
        return withMockUser -> {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UserDetails principal = User.withUsername(withMockUser.username())
                    .password(withMockUser.password())
                    .roles(withMockUser.roles())
                    .build();

            Collection<GrantedAuthority> authorities = Arrays.stream(withMockUser.roles())
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            return context;
        };
    }
}
