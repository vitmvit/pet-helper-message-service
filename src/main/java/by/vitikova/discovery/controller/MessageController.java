package by.vitikova.discovery.controller;

import by.vitikova.discovery.ErrorDto;
import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.create.MessageCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Messages", description = "API для работы с сообщениями в чатах")
public interface MessageController {

    @Operation(summary = "Получить все сообщения чата", description = "Возвращает поток сообщений для конкретного чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список сообщений успешно получен"),
            @ApiResponse(responseCode = "404", description = "Чат не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/{id}")
    Mono<ResponseEntity<Flux<MessageDto>>> findAllMessageByChatId(Long id);

    @Operation(summary = "Создать новое сообщение", description = "Создает сообщение и привязывает его к существующему чату")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сообщение создано"),
            @ApiResponse(responseCode = "404", description = "Чат для сообщения не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping
    Mono<ResponseEntity<MessageDto>> createMessage(MessageCreateDto messageCreateDto);

    @Operation(summary = "Удалить сообщение", description = "Удаляет сообщение по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сообщение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> deleteMessage(Long id);
}