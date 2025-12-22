package by.vitikova.discovery.service;

import by.vitikova.discovery.UserDto;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserDto> findByLogin(String login);
}