package by.vitikova.discovery.converter;

import by.vitikova.discovery.ChatDto;
import by.vitikova.discovery.create.ChatCreateDto;
import by.vitikova.discovery.model.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatConverter {

    ChatDto convert(Chat source);

    Chat convert(ChatCreateDto source);
}