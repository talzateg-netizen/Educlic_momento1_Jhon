// package com.cesde.educlic.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(
//                     "/scalar/**",
//                     "/swagger-ui/**",
//                     "/v3/api-docs/**",
//                     "/swagger-ui.html"
//                 ).permitAll()
//                 .anyRequest().permitAll() // Permite probar todos los endpoints sin bloqueo para el proyecto
//             );
//         return http.build();
//     }
// }