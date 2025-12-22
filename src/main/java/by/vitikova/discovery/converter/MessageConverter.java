package by.vitikova.discovery.converter;

import by.vitikova.discovery.MessageDto;
import by.vitikova.discovery.create.MessageCreateDto;
import by.vitikova.discovery.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageConverter {

    MessageDto convert(Message source);

    Message convert(MessageCreateDto source);
}