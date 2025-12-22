package by.vitikova.discovery.service;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.create.MessageCreateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService {

    Flux<MessageDto> findAllByChatId(Long id);

    Mono<MessageDto> create(MessageCreateDto dto);

    Mono<Void> delete(Long id);
}