package com.mois.authenticationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests  // Directly use the authorizeRequests method
                        .requestMatchers("/", "/home", "/public/**").permitAll()  // Public paths
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/profiles/login")  // Custom login page
                        .permitAll()  // Allow all users to see login page
                )
                .logout(LogoutConfigurer::permitAll// Allow all users to logout

                );

        return http.build();
    }
}