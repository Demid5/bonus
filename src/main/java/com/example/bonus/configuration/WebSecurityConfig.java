package com.example.bonus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic() // Включает базовую аутентификацию
                .and()
                .authorizeRequests() // Начинает определение правил авторизации
                .requestMatchers(HttpMethod.POST, "/api/**").hasRole("WRITER")
                .requestMatchers(HttpMethod.GET, "/api/**").hasRole("READER")
                .anyRequest().authenticated()
                .and()
                .csrf().disable() // Отключает защиту CSRF, что обычно безопасно для API
                .formLogin().disable(); // Отключает форму логина, предоставляемую Spring Security по умолчанию
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails reader = User.builder()
                .username("reader")
                .password(passwordEncoder().encode("1"))
                .roles("READER")
                .build();
        UserDetails writer = User.builder()
                .username("writer")
                .password(passwordEncoder().encode("2"))
                .roles("WRITER")
                .build();
        return new InMemoryUserDetailsManager(reader, writer);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
