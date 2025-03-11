package com.example.E_Commerce.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = extractToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password("")
                    .authorities(user.getRole().toString())
                    .build();

            SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities()));
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
