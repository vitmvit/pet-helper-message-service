package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.client.UserClient;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.converter.ChatConverter;
import by.vitikova.discovery.create.ChatCreateDto;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.exception.ResourceNotFoundException;
import by.vitikova.discovery.model.entity.Chat;
import by.vitikova.discovery.repository.ChatRepository;
import by.vitikova.discovery.repository.MessageRepository;
import by.vitikova.discovery.service.ChatService;
import by.vitikova.discovery.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserClient userClient;
    private final ChatConverter chatConverter;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Override
    public Mono<ChatDto> findById(Long id) {
        log.info("ChatService: find chat with messages, id: {}", id);
        return chatRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()))
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsBySupportName(String name) {
        log.info("ChatService: find chats by support with name: {}", name);
        return chatRepository.findBySupportName(name)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsByEmptySupportName() {
        log.info("ChatService: find chats by empty supportName");
        return chatRepository.findAll()
                .filter(chat -> StringUtils.isEmpty(chat.getSupportName()))
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsByUserName(String name) {
        return chatRepository.findByUserName(name)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsByUserNameContains(String name, String supportName) {
        log.info("ChatService: find chats by user with name: {}", name);
        return chatRepository.findByUserNameContainsAndSupportName(name, supportName)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsByStatus(ChatStatus status) {
        log.info("ChatService: find chats by status: {}", status.getStatus());
        return chatRepository.findByStatus(status)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsByType(ChatType type) {
        log.info("ChatService: find chats by type: {}", type.getType());
        return chatRepository.findByType(type)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsByTypeAndStatus(ChatType type, ChatStatus status) {
        log.info("ChatService: find chats by type: {}, status: {}", type.getType(), status.getStatus());
        return chatRepository.findByTypeAndStatus(type, status)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findChatsBySupportNameAndUserName(String supportName, String userName) {
        log.info("ChatService: find chats by user and support names");
        return chatRepository.findBySupportNameAndUserName(supportName, userName)
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Flux<ChatDto> findAll() {
        log.info("ChatService: find all chats");
        return chatRepository.findAll()
                .flatMap(this::enrichChatWithMessages)
                .map(chatConverter::convert);
    }

    @Override
    public Mono<ChatDto> create(ChatCreateDto dto) {
        log.info("ChatService: create chat");
        return validateUsersExistence(dto.getUserName(), dto.getSupportName())
                .then(Mono.just(dto)
                        .map(chatConverter::convert)
                        .flatMap(chatRepository::save)
                        .map(chatConverter::convert));
    }

    @Override
    @Transactional
    public Mono<ChatDto> updateStatus(Long id, ChatStatus status) {
        log.info("ChatService: update status by chatId: {}", id);
        return chatRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()))
                .flatMap(this::enrichChatWithMessages)
                .flatMap(chat -> {
                    chat.setStatus(status);
                    return chatRepository.save(chat);
                })
                .map(chatConverter::convert);
    }

    @Override
    @Transactional
    public Mono<ChatDto> updateSupport(Long id, String login) {
        return userClient.existsByLogin(login)
                .defaultIfEmpty(false)
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new EntityNotFoundException("Support user not found: " + login));
                    }

                    return chatRepository.findById(id)
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException()))
                            .flatMap(this::enrichChatWithMessages)
                            .flatMap(chat -> {
                                chat.setSupportName(login);
                                chat.setStatus(ChatStatus.OPEN);
                                return chatRepository.save(chat);
                            });
                })
                .map(chatConverter::convert);
    }

    @Override
    @Transactional
    public Mono<Void> deleteChatsByUserName(String login) {
        log.info("ChatService: mass deleting chats for user: {}", login);
        return chatRepository.findByUserName(login)
                .map(Chat::getId)
                .collectList()
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        return Mono.empty();
                    }
                    return messageRepository.deleteAllByChatIdIn(ids)
                            .then(chatRepository.deleteAllByUserName(login));
                });
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        log.info("ChatService: delete chat with id: {}", id);
        return messageRepository.deleteAllByChatId(id).then(chatRepository.deleteById(id));
    }

    private Mono<Void> validateUsersExistence(String userName, String supportName) {
        Mono<Void> userCheck = userClient.existsByLogin(userName)
                .flatMap(exists -> exists ? Mono.empty() :
                        Mono.error(new EntityNotFoundException("User not found: " + userName)))
                .then();

        Mono<Void> supportCheck = (supportName != null)
                ? userClient.existsByLogin(supportName)
                .flatMap(exists -> exists ? Mono.empty() :
                        Mono.error(new EntityNotFoundException("Support not found: " + supportName)))
                .then()
                : Mono.empty();

        return Mono.when(userCheck, supportCheck);
    }

    private Mono<Chat> enrichChatWithMessages(Chat chat) {
        return messageRepository.findByChatId(chat.getId())
                .collectList()
                .doOnNext(chat::setMessageList)
                .thenReturn(chat);
    }
}