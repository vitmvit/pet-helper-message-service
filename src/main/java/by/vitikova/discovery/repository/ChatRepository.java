package by.vitikova.discovery.repository;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.model.entity.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, Long> {

    Flux<Chat> findBySupportName(String name);

    Flux<Chat> findByUserName(String name);

    Flux<Chat> findByUserNameContainsAndSupportName(String name, String supportName);

    Flux<Chat> findByStatus(ChatStatus status);

    Flux<Chat> findByType(ChatType type);

    Flux<Chat> findByTypeAndStatus(ChatType type, ChatStatus status);

    Flux<Chat> findByStatusAndUpdateDateBefore(ChatStatus status, LocalDateTime dateTime);

    Flux<Chat> findBySupportNameAndUserName(String supportName, String userName);

    Mono<Void> deleteAllByUserName(String userName);
}