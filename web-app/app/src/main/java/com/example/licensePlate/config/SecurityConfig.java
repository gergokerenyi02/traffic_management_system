package com.example.licensePlate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable it later properly)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/detections", "/detections/**", "detections/save/**", "/register", "/styles.css", "/session/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/parking/**", "/parking", "/parking/exit/", "/parking/exit/**", "/session/validate").permitAll()
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .formLogin(form -> form.disable()) // Disable default login page
                .logout(logout -> logout.logoutUrl("/session/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID"));

        return http.build();
    }
}