package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.converter.MessageConverter;
import by.vitikova.discovery.create.MessageCreateDto;
import by.vitikova.discovery.exception.ResourceNotFoundException;
import by.vitikova.discovery.model.entity.Chat;
import by.vitikova.discovery.repository.ChatRepository;
import by.vitikova.discovery.repository.MessageRepository;
import by.vitikova.discovery.service.MessageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса сообщений.
 * <p>
 * Этот класс предоставляет методы для работы с сообщениями в чатах.
 * Он обеспечивает функциональность по поиску сообщений, созданию нового сообщения,
 * получению информации о сообщении по его идентификатору и удалению сообщения по его идентификатору.
 */
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private MessageRepository messageRepository;
    private ChatRepository chatRepository;
    private MessageConverter messageConverter;

    /**
     * Находит сообщение по заданному идентификатору.
     *
     * @param id идентификатор сообщения
     * @return объект типа MessageDto, содержащий информацию о сообщении
     * @throws ResourceNotFoundException если сообщение с заданным идентификатором не найдено
     */
    @Cacheable(value = "message", key = "#id")
    @Override
    public MessageDto findById(Long id) {
        logger.info("MessageService: find message with id: " + id);
        return messageConverter.convert(messageRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    /**
     * Находит список сообщений по идентификатору чата.
     *
     * @param id идентификатор чата
     * @return список объектов типа MessageDto, содержащих информацию о сообщениях
     */
    @Override
    public List<MessageDto> findAllByChatId(Long id) {
        logger.info("MessageService: find messages with chatId: " + id);
        var messageList = messageRepository.findByChatId(id);
        return messageList.stream().map(messageConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает новое сообщение в указанном чате.
     *
     * @param dto данные для создания сообщения
     * @return объект типа MessageDto, содержащий информацию о созданном сообщении
     * @throws ResourceNotFoundException если чат с заданным идентификатором не найден
     */
    @CacheEvict(value = "messages", key = "#dto.chatId")
    @Override
    public MessageDto create(MessageCreateDto dto) {
        logger.info("MessageService: create message in chat wih id: " + dto.getChatId());
        Chat chat = chatRepository.findById(dto.getChatId()).orElseThrow(ResourceNotFoundException::new);
        var message = messageConverter.convert(dto);
        message.setChat(chat);
        return messageConverter.convert(messageRepository.save(message));
    }

    /**
     * Удаляет сообщение по заданному идентификатору.
     *
     * @param id идентификатор сообщения
     */
    @CacheEvict(value = "messages", allEntries = true)
    @Override
    public void delete(Long id) {
        logger.info("MessageService: delete message with id: " + id);
        messageRepository.deleteById(id);
    }
}