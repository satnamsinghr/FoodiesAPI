package com.satnam.codesapi.user_service.config;

import com.satnam.codesapi.user_service.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Temporary permissive SecurityConfig to allow easy API testing.
 *
 * - All endpoints are currently permitted (no auth required).
 * - PasswordEncoder / AuthenticationProvider / AuthenticationManager beans are still defined,
 *   so you can re-enable authentication by uncommenting the jwtFilter wiring below.
 *
 * To re-enable JWT-based security later:
 *  1) Uncomment the JwtAuthenticationFilter wiring (autowire and addFilterBefore)
 *  2) Replace `.anyRequest().permitAll()` with appropriate `.authorizeHttpRequests(...)` rules
 *     (see your previous secure config)
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Keep these beans available (non-destructive). They are not used while permitAll is active,
    // but they will be ready if you re-enable authentication.
    @Autowired(required = false)
    private com.satnam.codesapi.user_service.config.JwtAuthenticationFilter jwtFilter;

    @Autowired(required = false)
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        if (userDetailsService != null) {
            p.setUserDetailsService(userDetailsService);
        }
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Permit all requests for testing (no auth). This is temporary.
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // keep register/login and swagger open explicitly (not required when using permitAll,
                        // but left for clarity)
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // For quick testing, allow everything
                        .anyRequest().permitAll()
                );

        // Keep the authentication provider wired (harmless while permitAll is used)
        http.authenticationProvider(authenticationProvider());

        // *** IMPORTANT ***
        // We intentionally DO NOT call:
        //   http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // This means the JwtAuthenticationFilter will not run and requests won't be checked.
        //
        // To re-enable JWT auth later:
        // 1) Ensure jwtFilter is non-null and available as a bean (remove 'required=false' above).
        // 2) Replace `.anyRequest().permitAll()` with your secured rules (e.g., .anyRequest().authenticated()).
        // 3) Uncomment the line below:
        //    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
