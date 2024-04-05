package by.vitikova.discovery.service;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.create.MessageCreateDto;

import java.util.List;

public interface MessageService {

    MessageDto findById(Long id);

    List<MessageDto> findAllByChatId(Long id);

    MessageDto create(MessageCreateDto dto);

    void delete(Long id);
}