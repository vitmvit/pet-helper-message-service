package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.converter.ChatConverter;
import by.vitikova.discovery.create.ChatCreateDto;
import by.vitikova.discovery.exception.ResourceNotFoundException;
import by.vitikova.discovery.model.entity.Chat;
import by.vitikova.discovery.repository.ChatRepository;
import by.vitikova.discovery.repository.MessageRepository;
import by.vitikova.discovery.service.ChatService;
import by.vitikova.discovery.util.StringUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса чата.
 *
 * Этот класс предоставляет методы для работы с чатами.
 * Он обеспечивает функциональность по поиску чатов, созданию нового чата и получению информации о чате по его идентификатору.
 */
@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;
    private ChatConverter chatConverter;
    private MessageRepository messageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    /**
     * Находит чат по заданному идентификатору.
     *
     * @param id идентификатор чата
     * @return объект типа ChatDto, содержащий информацию о чате
     * @throws ResourceNotFoundException если чат с заданным идентификатором не найден
     */
    @Cacheable(value = "chat", key = "#id")
    @Override
    public ChatDto findById(Long id) {
        logger.info("ChatService: find chat with id: " + id);
        return chatConverter.convert(chatRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    /**
     * Находит список чатов, связанных с указанным именем поддержки.
     *
     * @param name имя поддержки
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findChatsBySupportName(String name) {
        logger.info("ChatService: find chats by support with name: " + name);
        var list = chatRepository.findChatsBySupportName(name);
        return list.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Находит список чатов, у которых отсутствует имя поддержки.
     *
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findChatsByEmptySupportName() {
        logger.info("ChatService: find chats by empty supportName");
        var chatList = chatRepository.findAll();
        return chatList.stream().filter(chat -> StringUtils.isEmpty(chat.getSupportName())).map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Находит список чатов, связанных с указанным именем пользователя.
     *
     * @param name имя пользователя
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findChatsByUserName(String name) {
        logger.info("ChatService: find chats by user with name: " + name);
        var list = chatRepository.findChatsByUserName(name);
        return list.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Находит список чатов, связанных с указанным именем пользователя и поддержки.
     *
     * @param name имя пользователя
     * @param supportName имя поддержки
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findChatsByUserNameContains(String name,String supportName) {
        logger.info("ChatService: find chats by user with name: " + name);
        var list = chatRepository.findChatsByUserNameContainsAndSupportName(name, supportName);
        return list.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Находит список чатов, связанных с указанным статусом.
     *
     * @param status статус чата
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findChatsByStatus(ChatStatus status) {
        logger.info("ChatService: find chats by status: " + status.getStatus());
        var chatList = chatRepository.findChatsByStatus(status);
        return chatList.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Метод для поиска чатов по типу.
     *
     * @param type Тип чата, по которому необходимо произвести поиск.
     * @return Список чатов типа {@code ChatDto}, соответствующих указанному типу.
     */
    @Override
    public List<ChatDto> findChatsByType(ChatType type) {
        logger.info("ChatService: find chats by type: " + type.getType());
        var chatList = chatRepository.findChatsByType(type);
        return chatList.stream().map(chatConverter::convert).collect(Collectors.toList());

    }

    /**
     * Метод для поиска чатов по типу и статусу.
     *
     * @param type   Тип чата, по которому необходимо произвести поиск.
     * @param status Статус чата, по которому необходимо произвести поиск.
     * @return Список чатов типа {@code ChatDto}, соответствующих указанному типу и статусу.
     */
    @Override
    public List<ChatDto> findChatsByTypeAndStatus(ChatType type, ChatStatus status) {
        logger.info("ChatService: find chats by type: " + type.getType() + ", status: " + status.getStatus());
        var chatList = chatRepository.findChatsByTypeAndStatus(type, status);
        return chatList.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Находит список чатов, связанных с указанными логинами поддержки и пользователя.
     *
     * @param supportName имя поддержки
     * @param userName    имя пользователя
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findChatsBySupportNameAndUserName(String supportName, String userName) {
        logger.info("ChatService: find chats by user and support names");
        var list = chatRepository.findChatsBySupportNameAndUserName(supportName, userName);
        return list.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Находит все чаты.
     *
     * @return список объектов типа ChatDto, содержащих информацию о чатах
     */
    @Override
    public List<ChatDto> findAll() {
        logger.info("ChatService: find all chats");
        var chatList = chatRepository.findAll();
        return chatList.stream().map(chatConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает новый чат.
     *
     * @param dto данные для создания чата
     * @return объект типа ChatDto, содержащий информацию о созданном чате
     */
    @CacheEvict(value = "chats", key = "#dto.userName")
    @Transactional
    @Override
    public ChatDto create(ChatCreateDto dto) {
        logger.info("ChatService: create chat");
        var chat = chatConverter.convert(dto);
        return chatConverter.convert(chatRepository.save(chat));
    }

    /**
     * Обновляет статус чата по его идентификатору.
     *
     * @param id идентификатор чата
     * @param status новый статус чата
     * @return объект типа ChatDto, содержащий информацию о чате с обновленным статусом
     * @throws ResourceNotFoundException если чат с заданным идентификатором не найден
     */
    @Transactional
    @Override
    public ChatDto updateStatus(Long id, ChatStatus status) {
        logger.info("ChatService: update status by chatId: " + id);
        var chat = chatRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        chat.setStatus(status);
        return chatConverter.convert(chatRepository.save(chat));
    }

    /**
     * Обновляет имя поддержки чата по его идентификатору.
     *
     * @param id    идентификатор чата
     * @param login логин поддержки
     * @return объект типа ChatDto, содержащий информацию о чате с обновленным именем поддержки
     * @throws ResourceNotFoundException если чат с заданным идентификатором не найден
     */
    @Transactional
    @Override
    public ChatDto updateSupport(Long id, String login) {
        logger.info("ChatService: update support by chatId: " + id);
        var chat = chatRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        chat.setSupportName(login);
        return chatConverter.convert(chatRepository.save(chat));
    }

    /**
     * Удаление чатов по логину пользователя.
     *
     * @param login логин пользователя
     */
    @Transactional
    @Override
    public void deleteChatsByUserName(String login) {
        var chatList = chatRepository.findChatsByUserName(login);
        for (Chat item : chatList) {
            delete(item.getId());
        }
    }

    /**
     * Удаляет чат по его идентификатору.
     *
     * @param id идентификатор чата
     * @throws ResourceNotFoundException если чат с заданным идентификатором не найден
     */
    @CacheEvict(value = "chats", allEntries = true)
    @Transactional
    @Override
    public void delete(Long id) {
        logger.info("ChatService: dalete chat with id: " + id);
        messageRepository.deleteAllByChatId(id);
        chatRepository.deleteById(id);
    }
}