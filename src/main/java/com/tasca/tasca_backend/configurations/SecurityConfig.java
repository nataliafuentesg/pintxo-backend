package com.tasca.tasca_backend.configurations;
import com.tasca.tasca_backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;

    private static final String[] PUBLIC = {
            "/",
            "/error",
            "/actuator/health",
            "/api/auth/**",
            "/api/menu/**",
            "/api/Tapas5",
            "/api/drinks/**",
            "/api/contact/**",
            "/api/events-specials/**",
            "/api/tapas5/**",
            "/api/highlights/**",
            "/api/offers/**",
            "/api/public/**",
            "/images/**", "/assets/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(cs -> cs.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PUBLIC).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${tasca.cors.origins:}") String originsCsv
    ) {
        List<String> allowed = Arrays.stream(originsCsv.split("\\s*,\\s*"))
                .filter(s -> !s.isBlank())
                .toList();

        // Fallback en caso de que la env no esté seteada
        if (allowed.isEmpty()) {
            allowed = List.of(
                    "https://tascatapas.com",
                    "https://www.tascatapas.com",
                    "http://localhost:5175",
                    "http://127.0.0.1:5175"
            );
        }

        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(allowed);                        // orígenes EXACTOS (sin slash final)
        cfg.addAllowedOriginPattern("https://*.netlify.app");  // previews de Netlify
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization","Location","Content-Disposition"));

        // Si NO usas cookies/sesión cross-site, déjalo en false: simplifica preflights
        cfg.setAllowCredentials(false);

        cfg.setMaxAge(Duration.ofHours(2));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



