package com.mpt.journal.config;

import com.mpt.journal.controller.CustomAuthenticationFailureHandler;
import com.mpt.journal.service.UserDetailService; // Обратите внимание на изменение
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailService userDetailService; // Используем UserDetailService вместо UserService

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/reg", "/auth/logout").permitAll() // доступ для всех на страницы входа и регистрации
                        .requestMatchers("/users/**").hasRole("ADMIN") // доступ только для администраторов
                        .requestMatchers("/index", "/auth/logout-confirmation").authenticated() // доступ к этим страницам только для авторизованных пользователей
                        .anyRequest().authenticated() // все остальные запросы требуют аутентификации
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login") // страница входа
                        .failureHandler(customAuthenticationFailureHandler) // обработчик ошибок входа
                        .defaultSuccessUrl("/index", true) // перенаправление после успешного входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // URL для выхода
                        .logoutSuccessUrl("/auth/login")
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied") // страница, отображаемая при отказе в доступе
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/auth/login") // перенаправление на страницу входа при недействительной сессии
                        .maximumSessions(1) // максимальное количество сессий для пользователя
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
