package by.vitikova.discovery.converter;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.create.MessageCreateDto;
import by.vitikova.discovery.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Конвертер для преобразования объектов, связанных с сообщениями.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageConverter {

    /**
     * Преобразование объекта Message в объект MessageDto.
     *
     * @param source исходный объект Message
     * @return преобразованный объект MessageDto
     */
    MessageDto convert(Message source);

    /**
     * Преобразование объекта MessageCreateDto в объект Message.
     *
     * @param source исходный объект MessageCreateDto для создания сообщения
     * @return преобразованный объект Message
     */
    Message convert(MessageCreateDto source);
}
