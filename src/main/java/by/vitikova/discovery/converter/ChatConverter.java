package by.vitikova.discovery.converter;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.create.ChatCreateDto;
import by.vitikova.discovery.model.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Конвертер для преобразования объектов, связанных с чатами.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatConverter {

    /**
     * Преобразование объекта Chat в объект ChatDto.
     *
     * @param source исходный объект Chat
     * @return преобразованный объект ChatDto
     */
    ChatDto convert(Chat source);

    /**
     * Преобразование объекта ChatCreateDto в объект Chat.
     *
     * @param source исходный объект ChatCreateDto для создания чата
     * @return преобразованный объект Chat
     */
    Chat convert(ChatCreateDto source);
}
