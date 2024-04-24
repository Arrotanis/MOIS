

package com.mois.authenticationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsFilter corsFilter) throws Exception {
        http
                .addFilterBefore(corsFilter, CorsFilter.class)  // Injected CorsFilter
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/").permitAll()  // Public paths
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // Custom login page
                        .permitAll()  // Allow all users to see login page
                )
                .logout(LogoutConfigurer::permitAll);  // Allow all users to logout

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
        return new CorsFilter(corsConfigurationSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow localhost:3000
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "XMLHttpRequest"));

        source.registerCorsConfiguration("/**", config); // Apply CORS configuration to all paths
        return source;
    }
}
