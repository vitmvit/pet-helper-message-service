package by.vitikova.discovery.service;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.create.ChatCreateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatService {

    Mono<ChatDto> findById(Long id);

    Flux<ChatDto> findChatsBySupportName(String name);

    Flux<ChatDto> findChatsByEmptySupportName();

    Flux<ChatDto> findChatsByUserName(String name);

    Flux<ChatDto> findChatsByUserNameContains(String name, String supportName);

    Flux<ChatDto> findChatsByStatus(ChatStatus status);

    Flux<ChatDto> findChatsByType(ChatType type);

    Flux<ChatDto> findChatsByTypeAndStatus(ChatType type, ChatStatus status);

    Flux<ChatDto> findChatsBySupportNameAndUserName(String supportName, String userName);

    Flux<ChatDto> findAll();

    Mono<ChatDto> create(ChatCreateDto dto);

    Mono<ChatDto> updateStatus(Long id, ChatStatus status);

    Mono<ChatDto> updateSupport(Long id, String login);

    Mono<Void> deleteChatsByUserName(String login);

    Mono<Void> delete(Long id);
}