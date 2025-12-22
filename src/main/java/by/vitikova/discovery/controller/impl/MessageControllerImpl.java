package by.vitikova.discovery.controller.impl;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.controller.MessageController;
import by.vitikova.discovery.create.MessageCreateDto;
import by.vitikova.discovery.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageControllerImpl implements MessageController {

    private MessageService messageService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Flux<MessageDto>>> findAllMessageByChatId(@PathVariable("id") Long id) {
        return Mono.just(ResponseEntity.ok(messageService.findAllByChatId(id)));
    }

    @PostMapping
    public Mono<ResponseEntity<MessageDto>> createMessage(@RequestBody @Valid MessageCreateDto messageCreateDto) {
        return messageService.create(messageCreateDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMessage(@PathVariable("id") Long id) {
        return messageService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}