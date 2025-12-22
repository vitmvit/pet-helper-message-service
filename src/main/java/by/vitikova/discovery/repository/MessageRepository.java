package by.vitikova.discovery.repository;

import by.vitikova.discovery.model.entity.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MessageRepository extends
        ReactiveCrudRepository<Message, Long> {

    Flux<Message> findByChatId(Long id);

    Mono<Void> deleteAllByChatId(Long id);

    Mono<Void> deleteAllByChatIdIn(List<Long> ids);
}