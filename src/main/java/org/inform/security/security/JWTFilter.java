package org.inform.security.security;

import org.inform.security.exceptions.InvalidJwtAuthenticationException;
import org.inform.security.exceptions.JWTValidityExpiredException;
import org.inform.security.security.userdetails.CustomUserDetails;
import org.inform.security.security.userdetails.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    public JWTFilter(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String userLogin;
        String jwt;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            if (jwt.isEmpty()) {
                throw new InvalidJwtAuthenticationException("JWT token is empty");
            }
            userLogin = JWTUtil.extractLogin(jwt);
            if (JWTUtil.validateToken(jwt)) {
                CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userLogin);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                throw new JWTValidityExpiredException("JWT token expired");
            }
        }
        chain.doFilter(request, response);
    }
}
