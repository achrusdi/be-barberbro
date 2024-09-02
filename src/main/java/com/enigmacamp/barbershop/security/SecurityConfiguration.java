package com.enigmacamp.barbershop.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(accessDeniedHandler);
                    config.authenticationEntryPoint(authenticationEntryPoint);
                })
                .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v2/api-docs/**").permitAll()
                        .requestMatchers("/api/login", "/api/customer/register", "/api/barber/register", "/api/barbers",
                                "/api/barbers/{id}", "/api/payments/{id}/update", "/api/customers")
                        .permitAll()
                        .requestMatchers("/api/bookings/current").hasAnyAuthority("CUSTOMER", "STAFF")
                        .requestMatchers("/api/barber/**", "/api/bookings/{id}", "/api/bookings/{id}/cancel", "/api/bookings/{id}/complete", "/api/barbers/current").hasAnyAuthority("STAFF")
                        .requestMatchers("/api/payments/**", "/api/customers/current", "/api/reviews", "/api/barbers/nearby")
                        .hasAnyAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/customers/current").hasAnyAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/bookings").hasAnyAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/customers", "/api/bookings").hasAnyAuthority("ADMIN")
                        // .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/assets/**").permitAll()

                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
