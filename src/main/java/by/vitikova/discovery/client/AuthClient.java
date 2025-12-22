package by.vitikova.discovery.client;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {

    @PostExchange("/check")
    Mono<Boolean> check(@RequestHeader("Authorization") String auth);
}