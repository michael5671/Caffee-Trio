package com.ngntu10.configurations.Security;

//import com.ngntu10.security.JwtAuthenticationEntryPoint;
import com.ngntu10.security.JwtAuthenticationFilter;
import com.ngntu10.util.Constants;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
//@Profile("!mvcIt")
public class WebSecurityConfig {
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configure Spring Security.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> {
                        auth
                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/api-docs/**",
                                        "/api-docs.yaml",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/webjars/**",
                                        "/",
                                        "/api/auth/**",
                                        "/public/**",
                                        "/assets/**",
                                        "/swagger-ui/index.html",
                                        "/swagger-ui.html",
                                        "/swagger/**",
                                        "/swagger-resources/",
                                        "/configuration/ui",
                                        "/configuration/security"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/blogs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/documents/**").permitAll()
                                .requestMatchers("/admin/**").hasAuthority(Constants.RoleEnum.ADMIN.name())
                                .anyRequest().permitAll();
                    }
            )
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                return config;
            }))
            .csrf(AbstractHttpConfigurer::disable)
//            .exceptionHandling(configurer -> configurer
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            )
            .sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .headers(configurer -> configurer
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
