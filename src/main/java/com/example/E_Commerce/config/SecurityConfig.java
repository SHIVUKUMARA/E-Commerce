package com.example.E_Commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.E_Commerce.repository.UserRepository;
import com.example.E_Commerce.security.JwtAuthenticationFilter;
import com.example.E_Commerce.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/auth/**", "/api/store/all", "/api/store/product/all", "/email/send",
        "/api/payment/**", "/api/store/product", "/api/reviews/stats/**"
    };

    private static final String[] VENDOR_ENDPOINTS = {
        "/api/store/create", "/api/store/update/**", "/api/store/vendor/stores", 
        "/api/store/product/add/**", "/api/store/product/update/**", "/api/store/product/delete/**"
    };

    private static final String[] ADMIN_ENDPOINTS = {
        "/api/store/approve-reject/**", "/api/store/pending-requests",
        "/api/store/reject/**", "/api/store/admin/stores"
    };

    private static final String[] CUSTOMER_ENDPOINTS = {
        "/api/cart/**", "/api/address/**", "/api/orders/**",
        "/api/reviews/add/**", "/api/reviews/update/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(VENDOR_ENDPOINTS).hasAuthority("VENDOR")
                .requestMatchers(ADMIN_ENDPOINTS).hasAuthority("ADMIN")
                .requestMatchers(CUSTOMER_ENDPOINTS).hasAuthority("CUSTOMER")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userRepository),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
