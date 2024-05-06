package by.vitikova.discovery.service;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.create.ChatCreateDto;

import java.util.List;

public interface ChatService {

    ChatDto findById(Long id);

    List<ChatDto> findChatsBySupportName(String name);

    List<ChatDto> findChatsByEmptySupportName();

    List<ChatDto> findChatsByUserName(String name);

    List<ChatDto> findChatsByUserNameContains(String name, String supportName);

    List<ChatDto> findChatsByStatus(ChatStatus status);

    List<ChatDto> findChatsByType(ChatType type);

    List<ChatDto> findChatsByTypeAndStatus(ChatType type, ChatStatus status);

    List<ChatDto> findChatsBySupportNameAndUserName(String supportName, String userName);

    List<ChatDto> findAll();

    ChatDto create(ChatCreateDto dto);

    ChatDto updateStatus(Long id, ChatStatus status);

    ChatDto updateSupport(Long id, String login);

    void delete(Long id);
}