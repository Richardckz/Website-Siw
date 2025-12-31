package com.Siw.personalProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/index", "/contact", "/css/**", "/js/**", "/img/**", "/libraries/**", "/progetti/**", "/favicon.ico").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            // Login personalizzato
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .successHandler(successHandler())
            )

            // Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
public AuthenticationSuccessHandler successHandler() {
    return (request, response, authentication) -> {
        String redirect = request.getParameter("redirect");
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (redirect != null && !redirect.isEmpty()) {
            // Only allow admin redirects to admin paths
            if (redirect.startsWith("/admin") && isAdmin) {
                response.sendRedirect(redirect);
                return;
            }
        }

        response.sendRedirect(isAdmin ? "/admin/dashboard" : "/");
    };
}

}