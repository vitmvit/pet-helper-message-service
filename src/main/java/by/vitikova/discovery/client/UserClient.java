package by.vitikova.discovery.client;

import by.vitikova.discovery.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface UserClient {

    @GetExchange("/{login}")
    Mono<UserDto> findByLogin(@PathVariable("login") String login);

    @GetExchange("/exists/{login}")
    Mono<Boolean> existsByLogin(@PathVariable("login") String login);
}