package com.shohan.portfolio_ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000")); // Specify allowed origins
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        corsConfig.setAllowedHeaders(List.of("*")); // Allow all headers (or specify specific ones)
        corsConfig.setAllowCredentials(true); // Allow sending credentials (cookies, auth headers)
        corsConfig.setMaxAge(3600L); // Max age for preflight requests in seconds

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply this configuration to all paths

        return new CorsWebFilter(source);
    }
}
