package by.vitikova.discovery.filter;

import by.vitikova.discovery.client.AuthClient;
import by.vitikova.discovery.converter.UserConverter;
import by.vitikova.discovery.exception.InvalidJwtException;
import by.vitikova.discovery.model.entity.TokenPayload;
import by.vitikova.discovery.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter implements WebFilter {

    private final AuthClient authClient;
    private final UserService userService;
    private final UserConverter userConverter;
    private final ObjectMapper objectMapper;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
//        try {
//            // Проверяем, является ли запрос запросом на Swagger UI
//            if (request.getRequestURI().contains("/swagger-ui") || request.getRequestURI().contains("/api/doc") || request.getRequestURI().contains("/v3/api-docs")) {
//                // Если да, пропускаем фильтр и передаем запрос дальше
//                filterChain.doFilter(request, response);
//                return;
//            }
//            var token = this.recoverToken(request);
//            var login = getUsername(token);
//            User user = userConverter.convert(userService.findByLogin(login));
//            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            if (Boolean.FALSE.equals(authClient.check(token).getBody())) {
//                throw new InvalidJwtException(INVALID_TOKEN_ERROR);
//            }
//            filterChain.doFilter(request, response);
//        } catch (Exception e) {
//            throw new InvalidJwtException(INVALID_TOKEN_ERROR);
//        }
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Пропускаем Swagger
        if (path.contains("/swagger-ui") || path.contains("/api/doc") || path.contains("/v3/api-docs")) {
            return chain.filter(exchange);
        }

        String token = recoverToken(exchange);
        if (token == null) {
            return chain.filter(exchange);
        }

        return Mono.just(token)
                .flatMap(t -> authClient.check("Bearer " + t)
                        .defaultIfEmpty(false))
                .flatMap(checkResponse -> {
                    if (Boolean.FALSE.equals(checkResponse)) {
                        return Mono.error(new InvalidJwtException("Invalid token"));
                    }
                    log.info("CHECK: " + checkResponse);
                    String login = getUsername(token);
                    return userService.findByLogin(login)
                            .map(userConverter::convert)
                            .map(user -> new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()))
                            .flatMap(auth -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
                })
                .onErrorResume(e -> Mono.error(new InvalidJwtException("Auth failed: " + e.getMessage())));
    }

    private String getUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            TokenPayload tokenPayload = objectMapper.readValue(payload, TokenPayload.class);
            return tokenPayload.getUsername();
        } catch (Exception e) {
            throw new InvalidJwtException("Token parsing error");
        }
    }

    private String recoverToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}