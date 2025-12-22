package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.client.UserClient;
import by.vitikova.discovery.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public Mono<UserDto> findByLogin(String login) {
        log.info("UserService: find user by login: {}", login);
        return userClient.findByLogin(login)
                .onErrorResume(e -> {
                    log.error("UserService: Entity not found error for login: {}", login);
                    return Mono.error(new EntityNotFoundException());
                });
    }
}