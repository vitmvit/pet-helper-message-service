package by.vitikova.discovery.controller;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.create.ChatCreateDto;
import by.vitikova.discovery.create.MessageCreateDto;
import by.vitikova.discovery.service.ChatService;
import by.vitikova.discovery.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {
    private ChatService chatService;
    private MessageService messageService;

    @GetMapping("/{id}")
    public ResponseEntity<ChatDto> findChatById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findById(id));
    }

    @GetMapping("supportName/{name}")
    public ResponseEntity<List<ChatDto>> findChatsBySupportName(@PathVariable("name") String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatsBySupportName(name));
    }

    @GetMapping("free")
    public ResponseEntity<List<ChatDto>> findChatsByEmptySupportName() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatsByEmptySupportName());
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<ChatDto>> findChatsByStatus(@PathVariable("status") ChatStatus status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatsByStatus(status));
    }

    @GetMapping("userName/{name}")
    public ResponseEntity<List<ChatDto>> findChatsByUserName(@PathVariable("name") String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatsByUserName(name));
    }

    @GetMapping("userName/like/{name}/{supportName}")
    public ResponseEntity<List<ChatDto>> findChatsByUserNameContains(@PathVariable("name") String name, @PathVariable("supportName") String supportName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatsByUserNameContains(name, supportName));
    }

    @GetMapping("/{supportName}/{userName}")
    public ResponseEntity<List<ChatDto>> findChatsBySupportNameAndUserName(@PathVariable("supportName") String supportName, @PathVariable("userName") String userName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findChatsBySupportNameAndUserName(supportName, userName));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<List<MessageDto>> findAllMessageByChatId(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messageService.findAllByChatId(id));
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> findAllChats() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.findAll());
    }

    @PostMapping
    public ResponseEntity<ChatDto> createChat(@RequestBody ChatCreateDto chatCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chatService.create(chatCreateDto));
    }

    @PostMapping("/status/{id}/{status}")
    public ResponseEntity<ChatDto> updateStatusChat(@PathVariable("id") Long id,
                                                    @PathVariable("status") ChatStatus status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.updateStatus(id, status));
    }

    @PostMapping("/support/{id}/{login}")
    public ResponseEntity<ChatDto> updateSupportChat(@PathVariable("id") Long id,
                                                     @PathVariable("login") String login) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(chatService.updateSupport(id, login));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageCreateDto messageCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageService.create(messageCreateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable("id") Long id) {
        chatService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
        messageService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}