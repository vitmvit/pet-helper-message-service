package by.vitikova.discovery.config;

import by.vitikova.discovery.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableR2dbcAuditing
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, SecurityFilter securityFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/swagger-ui/**", "/api/doc/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/chats/**").hasAnyRole("SUPPORT", "USER")
                        .pathMatchers(HttpMethod.POST, "/api/v1/chats/**").hasAnyRole("SUPPORT", "USER")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/chats/**").hasAnyRole("SUPPORT", "USER")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/chats/**").hasAnyRole("SUPPORT", "USER")
                        .anyExchange().authenticated()
                )
                .addFilterAt(securityFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}