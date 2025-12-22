package by.vitikova.discovery.controller;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.create.ChatCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Chats", description = "API для управления чатами поддержки")
public interface ChatController {

    @Operation(summary = "Найти чат по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат найден"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    @GetMapping("/{id}")
    Mono<ResponseEntity<ChatDto>> findChatById(Long id);

    @Operation(summary = "Список чатов по имени сотрудника поддержки")
    @GetMapping("supportName/{name}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsBySupportName(String name);

    @Operation(summary = "Найти свободные чаты", description = "Возвращает чаты, у которых не назначен сотрудник поддержки")
    @GetMapping("free")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsByEmptySupportName();

    @Operation(summary = "Фильтр чатов по статусу")
    @GetMapping("status/{status}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsByStatus(ChatStatus status);

    @Operation(summary = "Фильтр чатов по типу")
    @GetMapping("type/{type}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsByType(ChatType type);

    @Operation(summary = "Поиск по типу и статусу одновременно")
    @GetMapping("status/{type}/{status}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsByTypeAndStatus(ChatType type, ChatStatus status);

    @Operation(summary = "Список чатов конкретного пользователя")
    @GetMapping("userName/{name}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsByUserName(String name);

    @Operation(summary = "Список чатов по специалисту поддержки")
    @GetMapping("userName/like/{name}/{supportName}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsByUserNameContains(String name, String supportName);

    @Operation(summary = "Поиск чатов по части имени пользователя и сотруднику")
    @GetMapping("/{supportName}/{userName}")
    Mono<ResponseEntity<Flux<ChatDto>>> findChatsBySupportNameAndUserName(String supportName, String userName);

    @Operation(summary = "Получить все чаты системы")
    @GetMapping
    Mono<ResponseEntity<Flux<ChatDto>>> findAllChats();

    @Operation(summary = "Создать новый чат", description = "Инициализирует новый чат. По умолчанию статус устанавливается через Listener (FREE)")
    @ApiResponse(responseCode = "201", description = "Чат создан")
    @PostMapping
    Mono<ResponseEntity<ChatDto>> createChat(ChatCreateDto chatCreateDto);

    @Operation(summary = "Обновить статус чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус обновлен"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    @PostMapping("/status/{id}/{status}")
    Mono<ResponseEntity<ChatDto>> updateStatusChat(Long id, ChatStatus status);

    @Operation(summary = "Назначить сотрудника поддержки на чат")
    @PostMapping("/support/{id}/{login}")
    Mono<ResponseEntity<ChatDto>> updateSupportChat(Long id, String login);

    @Operation(summary = "Массовое удаление чатов пользователя", description = "Удаляет все чаты и сообщения, связанные с логином пользователя")
    @ApiResponse(responseCode = "204", description = "Чаты удалены")
    @DeleteMapping("/users/{login}")
    Mono<ResponseEntity<Void>> deleteChatsByUserName(String login);

    @Operation(summary = "Удалить чат по ID", description = "Удаляет чат и все его сообщения")
    @ApiResponse(responseCode = "204", description = "Чат удален")
    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> deleteChat(Long id);
}