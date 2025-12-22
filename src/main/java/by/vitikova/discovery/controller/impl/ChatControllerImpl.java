package by.vitikova.discovery.controller.impl;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.controller.ChatController;
import by.vitikova.discovery.create.ChatCreateDto;
import by.vitikova.discovery.service.ChatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatControllerImpl implements ChatController {

    private ChatService chatService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ChatDto>> findChatById(@PathVariable("id") Long id) {
        return chatService.findById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("supportName/{name}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsBySupportName(@PathVariable("name") String name) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsBySupportName(name)));
    }

    @GetMapping("free")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsByEmptySupportName() {
        return Mono.just(ResponseEntity.ok(chatService.findChatsByEmptySupportName()));
    }

    @GetMapping("status/{status}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsByStatus(@PathVariable("status") ChatStatus status) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsByStatus(status)));
    }

    @GetMapping("type/{type}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsByType(@PathVariable("type") ChatType type) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsByType(type)));
    }

    @GetMapping("status/{type}/{status}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsByTypeAndStatus(@PathVariable("type") ChatType type,
                                                                        @PathVariable("status") ChatStatus status) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsByTypeAndStatus(type, status)));
    }

    @GetMapping("userName/{name}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsByUserName(@PathVariable("name") String name) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsByUserName(name)));
    }

    @GetMapping("userName/like/{name}/{supportName}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsByUserNameContains(@PathVariable("name") String name,
                                                                           @PathVariable("supportName") String supportName) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsByUserNameContains(name, supportName)));
    }

    @GetMapping("/{supportName}/{userName}")
    public Mono<ResponseEntity<Flux<ChatDto>>> findChatsBySupportNameAndUserName(@PathVariable("supportName") String supportName,
                                                                                 @PathVariable("userName") String userName) {
        return Mono.just(ResponseEntity.ok(chatService.findChatsBySupportNameAndUserName(supportName, userName)));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<ChatDto>>> findAllChats() {
        return Mono.just(ResponseEntity.ok(chatService.findAll()));
    }

    @PostMapping
    public Mono<ResponseEntity<ChatDto>> createChat(@RequestBody @Valid ChatCreateDto chatCreateDto) {
        return chatService.create(chatCreateDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PostMapping("/status/{id}/{status}")
    public Mono<ResponseEntity<ChatDto>> updateStatusChat(@PathVariable("id") Long id,
                                                          @PathVariable("status") ChatStatus status) {
        return chatService.updateStatus(id, status)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/support/{id}/{login}")
    public Mono<ResponseEntity<ChatDto>> updateSupportChat(@PathVariable("id") Long id,
                                                           @PathVariable("login") String login) {
        return chatService.updateSupport(id, login)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/users/{login}")
    public Mono<ResponseEntity<Void>> deleteChatsByUserName(@PathVariable("login") String login) {
        return chatService.deleteChatsByUserName(login)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteChat(@PathVariable("id") Long id) {
        return chatService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}