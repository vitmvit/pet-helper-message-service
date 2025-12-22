package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.client.UserClient;
import by.vitikova.discovery.converter.MessageConverter;
import by.vitikova.discovery.create.MessageCreateDto;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.exception.ResourceNotFoundException;
import by.vitikova.discovery.repository.ChatRepository;
import by.vitikova.discovery.repository.MessageRepository;
import by.vitikova.discovery.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserClient userClient;
    private final MessageConverter messageConverter;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    public Flux<MessageDto> findAllByChatId(Long id) {
        return messageRepository.findByChatId(id)
                .map(messageConverter::convert);
    }

    @Override
    public Mono<MessageDto> create(MessageCreateDto dto) {
        log.info("MessageService: create message in chat with id: {}", dto.getChatId());

        return validateSenderExistence(dto.getSenderName())
                .then(chatRepository.findById(dto.getChatId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException()))
                        .flatMap(chat -> {
                            var message = messageConverter.convert(dto);
                            message.setChatId(chat.getId());
                            return messageRepository.save(message);
                        })
                        .map(messageConverter::convert));
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return messageRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException()))
                .flatMap(message -> {
                    log.info("MessageService: delete message with id: {}", id);
                    return messageRepository.deleteById(id);
                });

//        if (message.getUuidPhoto() != null) {
//            //todo тут нормальная сага должна быть
//            imageClient.removeImage(message.getUuidPhoto());
//        }
    }

    private Mono<Void> validateSenderExistence(String senderName) {
        return userClient.existsByLogin(senderName)
                .defaultIfEmpty(false)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.empty()
                        : Mono.error(new EntityNotFoundException("Sender not found: " + senderName)))
                .then();
    }
}